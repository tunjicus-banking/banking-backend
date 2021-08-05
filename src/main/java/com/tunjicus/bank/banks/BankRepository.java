package com.tunjicus.bank.banks;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankRepository extends CrudRepository<Bank, Integer> {
    public Optional<Bank> findBankByName(String name);
}
