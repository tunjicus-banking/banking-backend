package com.tunjicus.bank.offers;

import com.tunjicus.bank.companies.CompanyRepository;
import com.tunjicus.bank.employmentHistory.EmploymentHistory;
import com.tunjicus.bank.employmentHistory.EmploymentHistoryRepository;
import com.tunjicus.bank.jobPostings.JobPosting;
import com.tunjicus.bank.jobPostings.JobPostingRepository;
import com.tunjicus.bank.jobPostings.exceptions.CompanyApplicationException;
import com.tunjicus.bank.offers.exceptions.InvalidOfferUserException;
import com.tunjicus.bank.offers.exceptions.OfferNotFoundException;
import com.tunjicus.bank.offers.exceptions.OfferRespondedToException;
import com.tunjicus.bank.users.UserRepository;
import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;

    Logger logger = LoggerFactory.getLogger(OfferService.class);

    public void acceptOffer(long offerId, int userId) {
        var offer = validatePreUpdate(offerId, userId);
        offer.setAccepted((short) 1);
        offerRepository.save(offer);

        var oPosting = jobPostingRepository.findById(offer.getJobPostingId());
        if (oPosting.isEmpty()) {
            logger.error(
                    String.format(
                            "Failed to find the job posting '%d' for offer '%d'",
                            offer.getJobPostingId(), offerId));

            // Just get rid of faulty offer
            offerRepository.deleteById(offerId);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }

        var posting = oPosting.get();
        employmentHistoryRepository.quitJobs(userId);
        employmentHistoryRepository.save(
                new EmploymentHistory(userId, posting.getPositionId(), offer.getSalary()));
    }

    void rejectOffer(long offerId, int userId) {
        var offer = validatePreUpdate(offerId, userId);
        offer.setAccepted((short) -1);
        offerRepository.save(offer);
    }

    Page<GetOfferDto> findAllByUserId(int userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        page = Math.max(page, 0);
        size = size < 0 ? 20 : size;
        return offerRepository
                .findAllByUserId(
                        userId, PageRequest.of(page, size, Sort.by("offerTime").descending()))
                .map(GetOfferDto::new);
    }

    public GetOfferDto create(JobPosting posting, int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        if (companyRepository.existsById(userId)) {
            throw new CompanyApplicationException();
        }

        var offer = new Offer(posting.getId(), userId, posting.getSalaryHigh());
        return new GetOfferDto(offerRepository.save(offer));
    }

    private Offer validatePreUpdate(long offerId, int userId) {
        var offer =
                offerRepository
                        .findById(offerId)
                        .orElseThrow(() -> new OfferNotFoundException(offerId));

        if (offer.getUserId() != userId) {
            throw new InvalidOfferUserException(offerId, userId);
        }

        if (offer.getAccepted() != 0) {
            throw new OfferRespondedToException(offerId);
        }

        return offer;
    }
}
