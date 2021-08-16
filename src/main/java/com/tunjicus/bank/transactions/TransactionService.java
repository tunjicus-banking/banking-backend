package com.tunjicus.bank.transactions;

import com.tunjicus.bank.accounts.AccountType;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.shared.MediaType;
import com.tunjicus.bank.transactions.dtos.GetTransactionDto;
import com.tunjicus.bank.transactions.dtos.PostTransactionDto;
import com.tunjicus.bank.transactions.exceptions.InsufficientFundsException;
import com.tunjicus.bank.transactions.exceptions.NoCheckingAccountException;
import com.tunjicus.bank.transactions.exceptions.SelfTransferException;
import com.tunjicus.bank.transactions.exceptions.TransactionException;
import com.tunjicus.bank.users.UserRepository;
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
    private final UserRepository userRepository;

    GetTransactionDto usersTransfer(PostTransactionDto transactionDto) {
        if (transactionDto.getFrom() == transactionDto.getTo()) {
            throw new SelfTransferException("self transfer via this method is not allowed");
        }
        var fromAccount =
                accountRepository.findByUserIdEqualsAndFundsGreaterThanEqualAndTypeEquals(
                        transactionDto.getFrom(),
                        transactionDto.getAmount(),
                        AccountType.CHECKING.label);
        var toAccount =
                accountRepository.findByUserIdEqualsAndTypeEquals(
                        transactionDto.getTo(), AccountType.CHECKING.label);

        if (fromAccount.isEmpty()) {
            throw new InsufficientFundsException("failed to find valid account for sending user");
        }

        if (toAccount.isEmpty()) {
            throw new NoCheckingAccountException("failed to find valid account for receiving user");
        }

        return saveTransaction(fromAccount.get(), toAccount.get(), transactionDto);
    }

//    Transaction selfTransfer(PostTransactionDto transactionDto) {
//        var fromAccount =
//                accountRepository.findByUserIdEqualsAndFundsGreaterThanEqual(
//                        transactionDto.getFrom(), transactionDto.getAmount());
//        var toAccount = accountRepository.findById(transactionDto.getTo());
//
//        if (fromAccount.isEmpty()) {
//            throw new InsufficientFundsException("failed to find valid sending account");
//        }
//
//        if (toAccount.isEmpty()) {
//            throw new TransactionException("failed to find to account");
//        }
//
//        return saveTransaction(fromAccount.get(), toAccount.get(), transactionDto);
//    }

    private GetTransactionDto saveTransaction(
            Account fromAccount, Account toAccount, PostTransactionDto transactionDto) {
        fromAccount.setFunds(fromAccount.getFunds().subtract(transactionDto.getAmount()));
        toAccount.setFunds(toAccount.getFunds().add(transactionDto.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        var info = new TransactionAccountInfo(fromAccount.getId(), fromAccount.getType(), toAccount.getId(), toAccount.getType());
        var t = transactionRepository.save(new Transaction(transactionDto, info));
        t.setTransactionTime(new Date());
        return new GetTransactionDto(t);
    }
}
