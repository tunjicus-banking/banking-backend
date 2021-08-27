package com.tunjicus.bank.accounts.dtos;

import com.tunjicus.bank.accounts.enums.AccountType;
import com.tunjicus.bank.accounts.enums.PostAccountType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class PostAccountDto {
    @NotNull(message = "bank id must not be null")
    @Positive(message = "bank id must be positive")
    private int bankId;

    @NotNull(message = "type must not be null")
    private PostAccountType type;
}
