package com.tunjicus.bank.transactions;

import com.tunjicus.bank.transactions.dtos.PostTransactionDto;
import com.tunjicus.bank.transactions.dtos.SelfTransferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long id;

    @Column
    private int fromUser;

    @Column
    private int toUser;

    @Column
    private int fromAccount;

    @Column
    @Nationalized
    private String fromType;

    @Column
    private int toAccount;

    @Column
    @Nationalized
    private String toType;

    @Column
    private BigDecimal amount;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionTime;

    public Transaction(PostTransactionDto transactionDto, TransactionAccountInfo info) {
        fromUser = transactionDto.getFrom();
        toUser = transactionDto.getTo();
        fromAccount = info.fromAccount();
        fromType = info.fromType();
        toAccount = info.toAccount();
        toType = info.toType();
        amount = transactionDto.getAmount();
    }

    public Transaction(SelfTransferDto transferDto, String fromType, String toType) {
        fromUser = transferDto.getUserId();
        toUser = transferDto.getUserId();
        fromAccount = transferDto.getFrom();
        this.fromType = fromType;
        toAccount = transferDto.getTo();
        this.toType = toType;
        amount = transferDto.getAmount();
    }
}
