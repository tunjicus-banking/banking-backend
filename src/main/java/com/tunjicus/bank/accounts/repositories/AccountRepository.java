package com.tunjicus.bank.accounts.repositories;

import com.tunjicus.bank.accounts.models.Account;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUserIdEqualsAndFundsGreaterThanEqualAndTypeEqualsAndClosedIsFalse(int id, BigDecimal amount, String type);
    Optional<Account> findByUserIdEqualsAndTypeEqualsAndClosedIsFalse(int id, String type);
    Optional<Account> findByUserIdEqualsAndFundsGreaterThanEqual(int id, BigDecimal amount);
    Optional<Account> findByUserIdEqualsAndIdEquals(int userId, int accountId);
    Optional<Account> findByIdAndClosedIsFalse(int id);
}
