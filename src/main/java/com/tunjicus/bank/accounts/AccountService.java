package com.tunjicus.bank.accounts;

import com.tunjicus.bank.accounts.dtos.GetAccountDto;
import com.tunjicus.bank.accounts.dtos.PostAccountDto;
import com.tunjicus.bank.accounts.enums.AccountType;
import com.tunjicus.bank.accounts.exceptions.AccountNotFoundException;
import com.tunjicus.bank.accounts.exceptions.IllegalAccountCreationException;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.models.CheckingAccount;
import com.tunjicus.bank.accounts.models.SavingsAccount;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.accounts.repositories.CheckingRepository;
import com.tunjicus.bank.accounts.repositories.SavingsRepository;
import com.tunjicus.bank.users.UserRepository;
import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CheckingRepository checkingRepository;
    private final SavingsRepository savingsRepository;

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    GetAccountDto findById(int id) {
        var account = accountRepository.findByIdAndClosedIsFalse(id);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(id);
        }

        return new GetAccountDto(account.get());
    }

    @Transactional
    GetAccountDto save(PostAccountDto accountDto) {
        var user = userRepository.findById(accountDto.getUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException(accountDto.getUserId());
        }

        var account = accountRepository.save(new Account(accountDto));
        createUnderlyingAccount(account.getType(), account.getId());
        account.setCreatedAt(new Date());
        return new GetAccountDto(account);
    }

    void delete(int id) {
        var oAccount = accountRepository.findByIdAndClosedIsFalse(id);
        if (oAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }

        var account = oAccount.get();
        account.setClosed(true);
        accountRepository.save(account);
    }

    private void createUnderlyingAccount(String type, int id) {
        switch (type) {
            case "C" -> checkingRepository.save(new CheckingAccount(id));
            case "S" -> savingsRepository.save(new SavingsAccount(id));
            default -> logger.error(String.format("Account %d was created with an invalid type of %s", id, type));
        }
    }
}
