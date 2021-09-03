package com.tunjicus.bank.transactions;

import com.tunjicus.bank.accounts.enums.AccountType;
import com.tunjicus.bank.accounts.exceptions.ValidAccountNotFoundException;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.auth.AuthService;
import com.tunjicus.bank.transactions.dtos.GetTransactionDto;
import com.tunjicus.bank.transactions.dtos.PostTransactionDto;
import com.tunjicus.bank.transactions.dtos.SelfTransferDto;
import com.tunjicus.bank.transactions.exceptions.InsufficientFundsException;
import com.tunjicus.bank.transactions.exceptions.NoCheckingAccountException;
import com.tunjicus.bank.transactions.exceptions.SelfTransferException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AuthService authService;

    GetTransactionDto usersTransfer(PostTransactionDto transactionDto) {
        if (transactionDto.getFrom() == transactionDto.getTo()) {
            throw new SelfTransferException("Self transfer via this method is not allowed");
        }

        var fromAccount =
                accountRepository
                        .findByUserIdEqualsAndFundsGreaterThanEqualAndTypeEqualsAndClosedIsFalse(
                                transactionDto.getFrom(),
                                transactionDto.getAmount(),
                                AccountType.CHECKING.label);

        var toAccount =
                accountRepository.findByUserIdEqualsAndTypeEqualsAndClosedIsFalse(
                        transactionDto.getTo(), AccountType.CHECKING.label);

        if (fromAccount.isEmpty()) {
            throw new InsufficientFundsException("Failed to find valid account for sending user");
        }

        if (toAccount.isEmpty()) {
            throw new NoCheckingAccountException("Failed to find valid account for receiving user");
        }

        return saveTransaction(fromAccount.get(), toAccount.get(), transactionDto);
    }

    public SelfTransferDto selfTransfer(SelfTransferDto dto) {
        if (dto.getFrom() == dto.getTo()) {
            throw new SelfTransferException("Cannot transfer between the same account");
        }

        var userId = authService.getCurrentUser().getId();

        var fromAccount =
                accountRepository
                        .findByUserIdEqualsAndIdEqualsAndFundsGreaterThanAndClosedIsFalse(
                                userId, dto.getFrom(), dto.getAmount())
                        .orElseThrow(
                                () ->
                                        new ValidAccountNotFoundException(
                                                userId, dto.getFrom()));

        var toAccount =
                accountRepository
                        .findByUserIdEqualsAndIdEqualsAndClosedIsFalse(userId, dto.getTo())
                        .orElseThrow(
                                () ->
                                        new ValidAccountNotFoundException(
                                                userId, dto.getTo()));

        fromAccount.setFunds(fromAccount.getFunds().subtract(dto.getAmount()));
        toAccount.setFunds(toAccount.getFunds().add(dto.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        var t =
                transactionRepository.save(
                        new Transaction(dto, fromAccount.getType(), toAccount.getType(), userId));
        t.setTransactionTime(new Date());

        return new SelfTransferDto(t);
    }

    private GetTransactionDto saveTransaction(
            Account fromAccount, Account toAccount, PostTransactionDto transactionDto) {
        fromAccount.setFunds(fromAccount.getFunds().subtract(transactionDto.getAmount()));
        toAccount.setFunds(toAccount.getFunds().add(transactionDto.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        var info =
                new TransactionAccountInfo(
                        fromAccount.getId(),
                        fromAccount.getType(),
                        toAccount.getId(),
                        toAccount.getType());
        var t = transactionRepository.save(new Transaction(transactionDto, info));
        t.setTransactionTime(new Date());
        return new GetTransactionDto(t);
    }
}
