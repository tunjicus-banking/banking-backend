package com.tunjicus.bank.companies.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class PostCompanyDto {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "description cannot be blank")
    private String description;

    @NotNull(message = "brand color cannot be null")
    @Positive(message = "brand color must be a positive in")
    private int brandColor;
}
