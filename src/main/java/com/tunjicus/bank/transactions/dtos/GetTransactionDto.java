package com.tunjicus.bank.transactions.dtos;

import com.tunjicus.bank.scheduled.TimeService;
import com.tunjicus.bank.transactions.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public final class GetTransactionDto {
    private String from;
    private String to;
    private String item;
    private BigDecimal amount;
    private Date transactionTime;

    public GetTransactionDto(DbTransactionDto dto) {
        from = dto.getFromUser();
        to = dto.getToUser();
        item = dto.getItem();
        amount = dto.getAmount();
        transactionTime = TimeService.calculateSimulatedDate(dto.getTransactionTime());
    }
}
