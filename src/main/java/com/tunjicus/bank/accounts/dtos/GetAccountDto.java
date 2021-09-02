package com.tunjicus.bank.accounts.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tunjicus.bank.accounts.models.Account;
import com.tunjicus.bank.accounts.enums.AccountType;
import com.tunjicus.bank.scheduled.TimeService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
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
        createdAt = TimeService.calculateSimulatedDate(account.getCreatedAt());
    }

    private static AccountType convertType(String type) {
        return switch (type) {
            case "C" -> AccountType.CHECKING;
            case "S" -> AccountType.SAVINGS;
            case "B" -> AccountType.BRAND;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GetAccountDto that = (GetAccountDto) o;

        if (getId() != that.getId()) return false;
        if (getUserId() != that.getUserId()) return false;
        if (getBankId() != that.getBankId()) return false;
        if (getType() != that.getType()) return false;
        return getFunds().equals(that.getFunds());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getUserId();
        result = 31 * result + getBankId();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getFunds().hashCode();
        result = 31 * result + getCreatedAt().hashCode();
        return result;
    }
}
