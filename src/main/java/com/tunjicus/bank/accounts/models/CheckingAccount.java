package com.tunjicus.bank.accounts.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigDecimal;

@Entity(name = "checking_accounts")
@IdClass(AccountId.class)
@Getter
@Setter
@NoArgsConstructor
public class CheckingAccount {
    @Id
    @Column(name = "account_id")
    private int id;

    @Id
    @Column
    @Nationalized
    private String type = "C";

    @Column
    private BigDecimal maintenanceFees = BigDecimal.valueOf(3);

    public CheckingAccount(int id) {
        this.id = id;
    }
}
