package com.tunjicus.bank.items.dtos;

import com.tunjicus.bank.items.Item;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class PostItemDto {
    @NotBlank(message = "name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "price cannot be null")
    private BigDecimal price;
}