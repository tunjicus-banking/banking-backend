package com.tunjicus.bank.accounts.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigDecimal;

@Entity(name = "savings_accounts")
@IdClass(AccountId.class)
@Getter
@Setter
@NoArgsConstructor
public class SavingsAccount {
    @Id
    @Column(name = "account_id")
    private int id;

    @Id
    @Column
    private String type = "S";

    @Column
    private byte transactionLimit = 6;

    @Column
    private BigDecimal interestRate = BigDecimal.ZERO;

    public SavingsAccount(int id) {
        this.id = id;
    }
}
