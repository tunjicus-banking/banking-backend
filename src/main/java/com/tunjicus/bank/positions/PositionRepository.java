package com.tunjicus.bank.positions;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PositionRepository extends PagingAndSortingRepository<Position, Integer> {
    Page<Position> findAllByCompanyIdAndActiveIsTrue(int companyId, Pageable pageable);
    Page<Position> findAllByActiveIsTrue(Pageable pageable);
}
