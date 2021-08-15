package com.tunjicus.bank.accounts.repositories;

import com.tunjicus.bank.accounts.models.AccountId;
import com.tunjicus.bank.accounts.models.CheckingAccount;
import org.springframework.data.repository.CrudRepository;

public interface CheckingRepository extends CrudRepository<CheckingAccount, AccountId> {
}
