package com.tunjicus.bank.accounts.dtos;

import com.tunjicus.bank.accounts.AccountType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class PostAccountDto {
    @NotNull(message = "user id must not be null")
    @Positive(message = "user id must be positive")
    private int userId;

    @NotNull(message = "bank id must not be null")
    @Positive(message = "bank id must be positive")
    private int bankId;

    @NotNull(message = "type must not be null")
    private AccountType type;
}
