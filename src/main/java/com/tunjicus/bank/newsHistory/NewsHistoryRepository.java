package com.tunjicus.bank.newsHistory;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface NewsHistoryRepository extends CrudRepository<NewsHistory, Integer> {
    Optional<NewsHistory> findTop1ByOrderByCreatedAtDesc();
}
