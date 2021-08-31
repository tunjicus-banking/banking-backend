package com.tunjicus.bank.items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemRepository extends PagingAndSortingRepository<Item, Integer> {
    boolean existsByName(String name);

    Page<Item> findAllByNameContains(String name, Pageable pageable);

    Page<Item> findAllByUserId(int userId, Pageable pageable);

    Page<Item> findAllByUserIdAndNameContains(int userId, String name, Pageable pageable);
}
