package com.tunjicus.bank.accounts.repositories;

import com.tunjicus.bank.accounts.models.AccountId;
import com.tunjicus.bank.accounts.models.SavingsAccount;
import org.springframework.data.repository.CrudRepository;

public interface SavingsRepository extends CrudRepository<SavingsAccount, AccountId> {}
