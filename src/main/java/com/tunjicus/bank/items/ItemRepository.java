package com.tunjicus.bank.items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ItemRepository extends PagingAndSortingRepository<Item, Integer> {
    boolean existsByName(String name);

    Page<Item> findAllByNameContains(String name, Pageable pageable);

    Page<Item> findAllByUserId(int userId, Pageable pageable);

    Page<Item> findAllByUserIdAndNameContains(int userId, String name, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update items set price = price * ?1")
    void updatePrices(BigDecimal modifier);
}
