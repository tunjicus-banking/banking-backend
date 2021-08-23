package com.tunjicus.bank.employmentHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EmploymentHistoryRepository
        extends PagingAndSortingRepository<EmploymentHistory, Long> {
    @Modifying
    @Transactional
    @Query(
            "update employment_history eh set eh.endDate = current_timestamp where eh.userId = ?1 and eh.endDate is null")
    void quitJobs(int userId);
    Page<EmploymentHistory> findAllByUserId(int id, Pageable pageable);
    boolean existsByUserIdAndPositionIdAndEndDateNull(int userId, int positionId);
}
