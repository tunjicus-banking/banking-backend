package com.tunjicus.bank.offers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "offers")
@Getter
@Setter
@NoArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private long id;

    @Column
    private int jobPostingId;

    @Column
    private int userId;

    @Column
    private BigDecimal salary;

    @Column
    private short accepted;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date offerTime;

    public Offer(int jobPostingId, int userId, BigDecimal salary) {
        this.jobPostingId = jobPostingId;
        this.userId = userId;
        this.salary = salary;
    }
}
