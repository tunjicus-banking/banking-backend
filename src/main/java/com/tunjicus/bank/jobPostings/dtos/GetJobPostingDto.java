package com.tunjicus.bank.jobPostings.dtos;

import com.tunjicus.bank.jobPostings.JobPosting;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class GetJobPostingDto {
    private final int id;
    private final int positionId;
    private final String description;
    private final BigDecimal salaryLow;
    private final BigDecimal salaryHigh;
    private final Date upSince;
    private final boolean active;

    public GetJobPostingDto(JobPosting jobPosting) {
        id = jobPosting.getId();
        positionId = jobPosting.getPositionId();
        description = jobPosting.getDescription();
        salaryLow = jobPosting.getSalaryLow();
        salaryHigh = jobPosting.getSalaryHigh();
        upSince = jobPosting.getUpSince();
        active = jobPosting.isActive();
    }
}
