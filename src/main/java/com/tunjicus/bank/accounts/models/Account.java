package com.tunjicus.bank.accounts.models;

import com.tunjicus.bank.accounts.dtos.PostAccountDto;
import com.tunjicus.bank.accounts.enums.AccountType;
import com.tunjicus.bank.accounts.enums.PostAccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int id;

    @Column
    @NotNull
    private int userId;

    @Column
    @NotNull
    private int bankId;

    @Column
    @NotBlank
    @Length(min = 1, max = 1)
    @Nationalized
    private String type;

    @Column
    @NotNull
    private BigDecimal funds;

    @Column
    private boolean closed;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Account() {}

    public Account(int userId, int bankId, PostAccountType type, BigDecimal funds) {
        this.userId = userId;
        this.bankId = bankId;
        this.type = postAccountTypeToString(type);
        this.funds = funds;
    }

    public Account(PostAccountDto accountDto, int userId) {
        this.userId = userId;
        bankId = accountDto.getBankId();
        type = postAccountTypeToString(accountDto.getType());
        funds = BigDecimal.valueOf(0);
    }

    private static String postAccountTypeToString(PostAccountType accountType) {
        return switch (accountType) {
            case SAVINGS -> AccountType.SAVINGS.label;
            case CHECKING -> AccountType.CHECKING.label;
        };
    }
}
