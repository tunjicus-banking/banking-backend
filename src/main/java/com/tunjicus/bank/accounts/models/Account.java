package com.tunjicus.bank.accounts.models;

import com.tunjicus.bank.accounts.dtos.PostAccountDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "accounts")
@Getter
@Setter
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

    public Account(PostAccountDto accountDto) {
        userId = accountDto.getUserId();
        bankId = accountDto.getBankId();
        type = accountDto.getType().label;
        funds = BigDecimal.valueOf(0);
    }
}
