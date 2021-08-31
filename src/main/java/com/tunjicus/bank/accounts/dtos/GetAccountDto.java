package com.tunjicus.bank.accounts.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.enums.AccountType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class GetAccountDto {
    private int id;
    private int userId;
    private int bankId;
    private AccountType type;
    private BigDecimal funds;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    public GetAccountDto(Account account) {
        id = account.getId();
        userId = account.getUserId();
        bankId = account.getBankId();
        type = convertType(account.getType());
        funds = account.getFunds();
        createdAt = account.getCreatedAt();
    }

    private static AccountType convertType(String type) {
        return switch (type) {
            case "C" -> AccountType.CHECKING;
            case "S" -> AccountType.SAVINGS;
            case "B" -> AccountType.BRAND;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
