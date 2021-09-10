package com.tunjicus.bank.newsHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsHistoryService {
    private final NewsHistoryRepository newsHistoryRepository;

    Page<GetNewsHistoryDto> findAll(int page, int size) {
        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;
        var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return newsHistoryRepository.findAll(pageable).map(GetNewsHistoryDto::new);
    }

    GetNewsHistoryDto findLatest() {
        var history = newsHistoryRepository.findTop1ByOrderByCreatedAtDesc();
        if (history.isEmpty()) return null;
        return new GetNewsHistoryDto(history.get());
    }
}
