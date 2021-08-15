package com.tunjicus.bank.transactions;

import com.tunjicus.bank.transactions.dtos.PostTransactionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "from_user")
    private int from;

    @Column(name = "to_user")
    private int to;

    @Column
    private BigDecimal amount;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transactionTime;

    public Transaction(PostTransactionDto transactionDto) {
        from = transactionDto.getFrom();
        to = transactionDto.getTo();
        amount = transactionDto.getAmount();
    }
}
