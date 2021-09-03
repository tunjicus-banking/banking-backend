package com.tunjicus.bank.scheduled;

import com.tunjicus.bank.accounts.enums.AccountType;
import com.tunjicus.bank.accounts.models.AccountId;
import com.tunjicus.bank.accounts.models.IdAndCompanyId;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.accounts.repositories.SavingsRepository;
import com.tunjicus.bank.employmentHistory.EmploymentHistory;
import com.tunjicus.bank.employmentHistory.EmploymentHistoryRepository;
import com.tunjicus.bank.transactions.Transaction;
import com.tunjicus.bank.transactions.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    // Run every "two weeks"
    private static final int salariesSchedule = TimeService.simulatedMinutesPerDay * 14 * 1000;

    // Run every "month"
    private static final int interestSchedule = TimeService.simulatedMinutesPerDay * 30 * 1000;

    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final SavingsRepository savingsRepository;
    Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Transactional
    @Scheduled(fixedRate = salariesSchedule, initialDelay = salariesSchedule)
    public void paySalaries() {
        logger.info("Running paySalaries");

        var history = employmentHistoryRepository.findAllByEndDateIsNull();
        for (EmploymentHistory employment : history) {
            var userAccountId = getUserAccount(employment.getUserId());
            if (userAccountId == null) {
                logger.warn(
                        String.format(
                                "Failed to find valid account for user %d",
                                employment.getUserId()));
                continue;
            }

            var companyAccount = getCompanyAccount(employment.getPositionId());
            if (companyAccount == null) {
                logger.warn(
                        String.format(
                                "Failed to find valid company account for position %d",
                                employment.getPositionId()));
                continue;
            }

            accountRepository.addMoney(employment.getSalary(), userAccountId.getId());
            var transaction =
                    new Transaction(
                            companyAccount.getCompanyId(),
                            userAccountId.getId(),
                            companyAccount.getId(),
                            AccountType.CHECKING.label,
                            userAccountId.getId(),
                            userAccountId.getType(),
                            employment.getSalary());
            transactionRepository.save(transaction);
        }

        logger.info("paySalaries task finished");
    }

    @Transactional
    @Scheduled(fixedRate = interestSchedule, initialDelay = interestSchedule)
    public void interestPayments() {
        logger.info("Applying interest payments");

        var savingsAccounts = savingsRepository.findAll();
        for (var account : savingsAccounts) {
            var mainAccount = accountRepository.findById(account.getId()).orElse(null);
            if (mainAccount == null) {
                logger.warn(
                        String.format("Failed to find main account with id %d", account.getId()));
                continue;
            }

            if (account.getInterestRate().compareTo(BigDecimal.ZERO) == 0) continue;
            var interest = mainAccount.getFunds().multiply(account.getInterestRate());
            mainAccount.setFunds(mainAccount.getFunds().add(interest));
            accountRepository.save(mainAccount);
        }

        logger.info("interestPayments task finished");
    }

    private IdAndCompanyId getCompanyAccount(int positionId) {
       return accountRepository.findCompanyAccount(positionId).orElse(null);
    }

    private AccountId getUserAccount(int userId) {
        var accountsToCheck = new String[] {AccountType.CHECKING.label, AccountType.SAVINGS.label};

        for (var accountType : accountsToCheck) {
            var account =
                    accountRepository.findByUserIdAndTypeAndClosedIsFalse(userId, accountType);
            if (account.isPresent()) {
                return new AccountId(account.get().getId(), accountType);
            }
        }

        return null;
    }
}
