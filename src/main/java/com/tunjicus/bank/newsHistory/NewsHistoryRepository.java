package com.tunjicus.bank.newsHistory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface NewsHistoryRepository extends PagingAndSortingRepository<NewsHistory, Integer> {
    Optional<NewsHistory> findTop1ByOrderByCreatedAtDesc();
}
