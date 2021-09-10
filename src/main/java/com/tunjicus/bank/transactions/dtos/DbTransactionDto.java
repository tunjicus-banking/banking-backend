package com.tunjicus.bank.transactions.dtos;

import java.math.BigDecimal;
import java.util.Date;

public interface DbTransactionDto {
    String getFromUser();
    String getToUser();
    String getItem();
    BigDecimal getAmount();
    Date getTransactionTime();
}
