package com.tunjicus.bank.jobPostings.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class PostJobPostingDto {
    @NotNull(message = "position id cannot be null")
    @Positive(message = "position id must be position")
    private int positionId;

    @NotBlank(message = "description cannot be blank")
    private String description;

    @NotNull(message = "salary cannot be null")
    @Positive(message = "salary must be positive")
    private BigDecimal salaryLow;

    @NotNull(message = "salary cannot be null")
    @Positive(message = "salary must be positive")
    private BigDecimal salaryHigh;

    private boolean active = true;
}
