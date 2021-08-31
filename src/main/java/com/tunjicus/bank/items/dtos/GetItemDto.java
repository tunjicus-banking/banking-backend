package com.tunjicus.bank.items.dtos;

import com.tunjicus.bank.items.Item;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class GetItemDto {
    private final int id;
    private final String name;
    private final String description;
    private final BigDecimal price;

    public GetItemDto(Item item) {
        id = item.getId();
        name = item.getName();
        description = item.getDescription();
        price = item.getPrice();
    }
}
