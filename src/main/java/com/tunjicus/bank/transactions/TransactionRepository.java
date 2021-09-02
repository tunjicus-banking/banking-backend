package com.tunjicus.bank.transactions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Page<Transaction> findAllByFromUserOrToUser(int from, int to, Pageable pageable);
}
