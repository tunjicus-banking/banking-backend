package com.tunjicus.bank.employmentHistory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "employment_history")
@Setter
@Getter
@NoArgsConstructor
public class EmploymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employment_history_id")
    private long id;

    @Column
    private int userId;

    @Column
    private int positionId;

    @Column
    private BigDecimal salary;

    @Column(insertable = false, updatable = false)
    private Date hireDate;

    @Column(insertable = false)
    private Date endDate;

    public EmploymentHistory(int userId, int positionId, BigDecimal salary) {
        this.userId = userId;
        this.positionId = positionId;
        this.salary = salary;
    }
}
