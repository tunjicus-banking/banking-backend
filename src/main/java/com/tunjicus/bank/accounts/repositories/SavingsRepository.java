package com.tunjicus.bank.accounts.repositories;

import com.tunjicus.bank.accounts.models.AccountId;
import com.tunjicus.bank.accounts.models.SavingsAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface SavingsRepository extends CrudRepository<SavingsAccount, AccountId> {
    @Modifying
    @Transactional
    @Query("update savings_accounts set interestRate = interestRate * ?1")
    void updateInterestRates(BigDecimal modifier);
}
