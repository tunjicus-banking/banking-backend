package com.tunjicus.bank.employmentHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmploymentHistoryRepository
        extends PagingAndSortingRepository<EmploymentHistory, Long> {
    @Modifying
    @Transactional
    @Query(
            "update employment_history eh set eh.endDate = current_timestamp where eh.userId = ?1 and eh.endDate is null")
    void quitJobs(int userId);

    Page<EmploymentHistory> findAllByUserId(int id, Pageable pageable);

    Iterable<EmploymentHistory> findAllByEndDateIsNull();

    boolean existsByUserIdAndPositionIdAndEndDateNull(int userId, int positionId);

    Optional<EmploymentHistory> findByUserIdAndEndDateIsNull(int id);

    @Query(
            value =
                    "select sum(datediff_big(ss, hire_date, end_date)) sum "
                            + "from employment_history where user_id = ?1",
            nativeQuery = true)
    Optional<Long> getExperience(int userId);

    @Query(
            value =
                    "select sum(datediff_big(ss, hire_date, getdate())) as sum "
                            + "from employment_history where user_id = ?1 and end_date is null",
            nativeQuery = true)
    Optional<Long> getCurrentJobExperience(int userId);

    @Query(
            value =
                    "select sum(datediff_big(ss, hire_date, getdate())) as sum "
                            + "from employment_history eh "
                            + "INNER JOIN positions p on p.position_id = eh.position_id "
                            + "where user_id = ?1 and end_date is null and company_id = ?2",
            nativeQuery = true)
    Optional<Long> getCurrentJobExperienceAtCompany(int userId, int companyId);
    @Query(
            value =
                    "select sum(datediff_big(ss, hire_date, getdate())) as sum "
                            + "from employment_history eh "
                            + "INNER JOIN positions p on p.position_id = eh.position_id "
                            + "where user_id = ?1 and company_id = ?2",
            nativeQuery = true)
    Optional<Long> getExperienceAtCompany(int userId, int companyId);

    @Query("select p.companyId from employment_history eh " +
            "inner join positions p on p.id = eh.positionId " +
            "where eh.userId = ?1 and eh.endDate is null ")
    Optional<Integer> getCurrentCompany(int userId);
}
