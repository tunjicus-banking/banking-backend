package com.tunjicus.bank.transactions.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class PostTransactionDto {
    @NotNull(message = "from cannot be null")
    @Positive(message = "from must be positive")
    private int from;

    @NotNull(message = "to cannot be null")
    @Positive(message = "to must be positive")
    private int to;

    @NotNull(message = "amount cannot be null")
    @DecimalMin(value = "0", inclusive = false, message = "amount must be greater than 0")
    private BigDecimal amount;
}
