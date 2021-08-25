package com.tunjicus.bank.offers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OfferRepository extends PagingAndSortingRepository<Offer, Long> {
    Page<Offer> findAllByUserId(int id, Pageable pageable);
    boolean existsByUserIdAndJobPostingIdAndAccepted(int userId, int jobPostingId, short accepted);
}
