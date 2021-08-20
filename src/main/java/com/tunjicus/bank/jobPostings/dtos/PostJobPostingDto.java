package com.tunjicus.bank.jobPostings.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class PostJobPostingDto {
    @NotNull(message = "position id cannot be null")
    @Positive(message = "position id must be position")
    private int positionId;

    @NotBlank(message = "description cannot be blank")
    private String description;

    private boolean active = true;
}
