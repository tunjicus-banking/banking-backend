package com.tunjicus.bank.transactions;

import com.tunjicus.bank.transactions.dtos.DbTransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    @Query(
            """
                select u.username as fromUser, u2.username as toUser,
                       i.name as item, t.amount as amount, t.transactionTime as transactionTime
                from transactions t
                inner join users u on u.id = t.fromUser
                inner join users u2 on u2.id = t.toUser
                left outer join items i on t.itemId = i.id
                where t.fromUser = ?1 or t.toUser = ?1
            """)
    Page<DbTransactionDto> findAllUserTransactions(int userId, Pageable pageable);

    @Query(
            """
                select u.username as fromUser, u2.username as toUser,
                       i.name as item, t.amount as amount, t.transactionTime as transactionTime
                from transactions t
                inner join users u on u.id = t.fromUser
                inner join users u2 on u2.id = t.toUser
                left outer join items i on t.itemId = i.id
                where t.id = ?1
            """)
    Optional<DbTransactionDto> getDbDto(long transactionId);
}
