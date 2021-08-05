package com.tunjicus.bank.accounts.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PostAccountDto {
    @NotNull(message = "user id must not be null")
    @Positive(message = "user id must be positive")
    private int userId;

    @NotNull
    private BigDecimal funds;
}
