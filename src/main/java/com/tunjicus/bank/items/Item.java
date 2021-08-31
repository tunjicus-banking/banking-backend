package com.tunjicus.bank.items;

import com.tunjicus.bank.items.dtos.PostItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "items")
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;

    @Column private int userId;

    @Column private String name;

    @Column private String description;

    @Column private BigDecimal price;

    public Item(PostItemDto dto, int userId) {
        this.userId = userId;
        name = dto.getName();
        description = dto.getDescription();
        price = dto.getPrice();
    }
}
