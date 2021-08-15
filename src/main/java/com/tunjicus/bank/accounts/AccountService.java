package com.tunjicus.bank.accounts;

import com.tunjicus.bank.accounts.dtos.GetAccountDto;
import com.tunjicus.bank.accounts.dtos.PostAccountDto;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.models.CheckingAccount;
import com.tunjicus.bank.accounts.models.SavingsAccount;
import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.accounts.repositories.CheckingRepository;
import com.tunjicus.bank.accounts.repositories.SavingsRepository;
import com.tunjicus.bank.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CheckingRepository checkingRepository;
    private final SavingsRepository savingsRepository;

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    ArrayList<GetAccountDto> findAll() {
        var accounts = accountRepository.findAll();
        ArrayList<GetAccountDto> accountDtos = new ArrayList<>();

        accounts.forEach(a -> accountDtos.add(new GetAccountDto(a)));

        return accountDtos;
    }

    Optional<GetAccountDto> findById(int id) {
        var account = accountRepository.findById(id);
        if (account.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new GetAccountDto(account.get()));
    }

    @Transactional
    GetAccountDto save(PostAccountDto accountDto) throws InvalidUserException {
        var user = userRepository.findById(accountDto.getUserId());
        if (user.isEmpty()) {
            throw new InvalidUserException();
        }

        var account = accountRepository.save(new Account(accountDto));
        createUnderlyingAccount(account.getType(), account.getId());
        account.setCreatedAt(new Date());
        return new GetAccountDto(account);
    }

    private void createUnderlyingAccount(String type, int id) {
        switch (type) {
            case "C" -> checkingRepository.save(new CheckingAccount(id));
            case "S" -> savingsRepository.save(new SavingsAccount(id));
            default -> logger.error(String.format("Account %d was created with an invalid type of %s", id, type));
        }
    }
}
