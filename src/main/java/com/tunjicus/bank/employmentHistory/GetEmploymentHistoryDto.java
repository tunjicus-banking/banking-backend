package com.tunjicus.bank.employmentHistory;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class GetEmploymentHistoryDto {
    private final int positionId;
    private final BigDecimal salary;
    private final Date hireDate;
    private final Date endDate;

    public GetEmploymentHistoryDto(EmploymentHistory employmentHistory) {
        positionId = employmentHistory.getPositionId();
        salary = employmentHistory.getSalary();
        hireDate = employmentHistory.getHireDate();
        endDate = employmentHistory.getEndDate();
    }
}
