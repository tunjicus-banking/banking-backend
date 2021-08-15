package com.tunjicus.bank.transactions.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetSelfTransferDto {
    private int userId;
    private int from;
    private int to;
    private BigDecimal amount;
}
