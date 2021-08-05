package com.tunjicus.bank.accounts.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class GetAccountDto {
    private BigDecimal funds;
    private Date createdAt;
}
