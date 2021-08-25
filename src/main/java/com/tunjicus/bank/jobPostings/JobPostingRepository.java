package com.tunjicus.bank.jobPostings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JobPostingRepository extends PagingAndSortingRepository<JobPosting, Integer> {
    @Query(
            value =
                    "select jp from positions p "
                            + "inner join job_postings jp on p.id = jp.positionId "
                            + "where p.companyId = ?1 and p.active = true")
    Page<JobPosting> findAllByCompanyIdIncludeAll(int id, Pageable pageable);

    @Query(
            value =
                    "select jp from positions p "
                            + "inner join job_postings jp on p.id = jp.positionId "
                            + "where p.companyId = ?1 and p.active = true and jp.active = true")
    Page<JobPosting> findAllByCompanyId(int id, Pageable pageable);

    Page<JobPosting> findAllByPositionIdAndActiveIsTrue(int id, Pageable pageable);

    Page<JobPosting> findAllByPositionId(int id, Pageable pageable);

    Page<JobPosting> findAllByActiveIsTrue(Pageable pageable);

    void deleteAllByPositionId(int id);

    @Query(
            "select p.companyId from job_postings jp "
                    + "inner join positions p on p.id = jp.positionId "
                    + "where jp.id = ?1")
    int getCompanyId(int id);
}
