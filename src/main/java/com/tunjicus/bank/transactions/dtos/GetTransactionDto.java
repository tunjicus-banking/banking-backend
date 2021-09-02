package com.tunjicus.bank.transactions.dtos;

import com.tunjicus.bank.scheduled.TimeService;
import com.tunjicus.bank.transactions.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Getter
public final class GetTransactionDto {
    private final int from;
    private final int to;
    private final BigDecimal amount;
    private final Date transactionTime;

    public GetTransactionDto(Transaction transaction) {
        from = transaction.getFromUser();
        to = transaction.getToUser();
        amount = transaction.getAmount();
        transactionTime = TimeService.calculateSimulatedDate(transaction.getTransactionTime());
    }
}
