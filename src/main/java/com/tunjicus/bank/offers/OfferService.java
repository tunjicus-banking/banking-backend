package com.tunjicus.bank.offers;

import com.tunjicus.bank.accounts.repositories.AccountRepository;
import com.tunjicus.bank.companies.CompanyRepository;
import com.tunjicus.bank.employmentHistory.EmploymentHistory;
import com.tunjicus.bank.employmentHistory.EmploymentHistoryRepository;
import com.tunjicus.bank.jobPostings.JobPosting;
import com.tunjicus.bank.jobPostings.JobPostingRepository;
import com.tunjicus.bank.jobPostings.exceptions.CompanyApplicationException;
import com.tunjicus.bank.offers.exceptions.InvalidOfferUserException;
import com.tunjicus.bank.offers.exceptions.OfferNotFoundException;
import com.tunjicus.bank.offers.exceptions.OfferRespondedToException;
import com.tunjicus.bank.requirements.RequirementRepository;
import com.tunjicus.bank.requirements.models.Requirement;
import com.tunjicus.bank.requirements.models.RequirementEnum;
import com.tunjicus.bank.requirements.models.RequirementInfo;
import com.tunjicus.bank.requirements.models.RequirementSpecification;
import com.tunjicus.bank.users.UserRepository;
import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final AccountRepository accountRepository;
    private final RequirementRepository requirementRepository;

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

        var salary = posting.getSalaryHigh();
        var requirement = requirementRepository.findById(posting.getId());
        if (requirement.isPresent()) {
            salary = calculateSalary(requirement.get(), posting, userId);
        }

        var offerStatus = OfferStatus.OFFERED;
        if (salary.compareTo(BigDecimal.ZERO) == 0) {
            offerStatus = OfferStatus.REJECTED;
        }

        var offer = new Offer(posting.getId(), userId, salary, offerStatus);
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

    private BigDecimal calculateSalary(Requirement requirement, JobPosting posting, int userId) {
        var percentage = BigDecimal.ZERO;
        var requirementEnumLength = RequirementEnum.values().length;
        var requirementsLength = requirement.getInfo().size();
        var companyId = jobPostingRepository.getCompanyId(posting.getId());

        if (requirementEnumLength < requirementsLength) {
            logger.error(String.format("Requirement enum length: %d. Found %d requirements for requirement %d", requirementEnumLength, requirementsLength, requirement.getId()));
        } else if (requirementEnumLength > requirementsLength) {
            logger.warn(String.format("Requirement enum length: %d. Found %d requirements for requirement %d", requirementEnumLength, requirementsLength, requirement.getId()));
            percentage = percentage.add(BigDecimal.valueOf((requirementEnumLength - requirementsLength) * 100L));
        }

        var oneHundred = BigDecimal.valueOf(100);
        for (RequirementInfo info : requirement.getInfo()) {
            var specification = RequirementSpecification.fromString(info.getSpecification());
            if (specification.equals(RequirementSpecification.N)) {
                percentage = percentage.add(oneHundred);
                continue;
            }

            var requirementEnum = RequirementEnum.fromString(info.getRequirement());
            ImmutablePair<BigDecimal, BigDecimal> values = switch (requirementEnum) {
                case CURRENT_SALARY -> new ImmutablePair<>(requirement.getCurrentSalary(), getCurrentSalary(userId));
                case NET_WORTH -> new ImmutablePair<>(requirement.getNetWorth(), getNetWorth(userId));
                case EXPERIENCE -> new ImmutablePair<>(BigDecimal.valueOf(requirement.getExperience()), getExperience(userId));
                case COMPANY_EXPERIENCE -> new ImmutablePair<>(BigDecimal.valueOf(requirement.getCompanyExperience()), getCompanyExperience(userId, companyId));
            };

            logger.info(String.format("Requirement: %s, values: %s", requirementEnum, values));

            if (values.left.compareTo(BigDecimal.ZERO) == 0) {
                percentage = percentage.add(oneHundred);
                continue;
            }

            if (values.right.compareTo(BigDecimal.ZERO) == 0) {
                if (specification.equals(RequirementSpecification.R)) {
                    return BigDecimal.ZERO;
                }
                continue;
            }

            var fieldPercent = values.right.divide(values.left, 5, RoundingMode.HALF_EVEN).multiply(oneHundred);
            logger.info(String.format("Field percent %s", fieldPercent));
            if (fieldPercent.compareTo(oneHundred) >= 0) {
                percentage = percentage.add(oneHundred);
            } else {
                if (specification.equals(RequirementSpecification.R)) {
                    return BigDecimal.ZERO;
                }
                percentage = percentage.add(fieldPercent);
            }
        }

        percentage = percentage.divide(BigDecimal.valueOf(requirementEnumLength), 5, RoundingMode.HALF_EVEN);

        logger.info(String.format("Percentage: %s, Required: %s", percentage, requirement.getRequiredCompletion()));

        if (percentage.compareTo(BigDecimal.valueOf(requirement.getRequiredCompletion())) < 0) {
            // reject offer
            return BigDecimal.ZERO;
        }


        var salary = percentage.divide(oneHundred, 5, RoundingMode.HALF_EVEN).multiply(posting.getSalaryHigh());
        if (salary.compareTo(posting.getSalaryLow()) < 0) {
            return posting.getSalaryLow();
        }

        // don't apply modifier if 100% match
        if (percentage.compareTo(oneHundred) == 0) return salary;

        salary = applyModifier(salary, requirement.getModifier());
        if (salary.compareTo(posting.getSalaryLow()) < 0) {
            return posting.getSalaryLow();
        }
        return salary;
    }

    private BigDecimal applyModifier(BigDecimal salary, byte modifier) {
        if (modifier == 0) return salary;
        return BigDecimal.valueOf(modifier).multiply(salary).divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
    }

    private BigDecimal getCurrentSalary(int userId) {
        var history = employmentHistoryRepository.findByUserIdAndEndDateIsNull(userId);
        if (history.isEmpty()) return BigDecimal.ZERO;
        return history.get().getSalary();
    }

    private BigDecimal getNetWorth(int userId) {
        return accountRepository.getNetWorth(userId).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getExperience(int userId) {
        var experience = employmentHistoryRepository.getExperience(userId).orElse(0L);
        var currentJobExperience = employmentHistoryRepository.getCurrentJobExperience(userId).orElse(0L);
        return BigDecimal.valueOf(experience + currentJobExperience);
    }

    private BigDecimal getCompanyExperience(int userId, int companyId) {
        var currentCompany = employmentHistoryRepository.getCurrentCompany(userId).orElse(-1);

        long currentCompanyJobExperience = 0;
        if (currentCompany == companyId) {
            currentCompanyJobExperience = employmentHistoryRepository.getCurrentJobExperienceAtCompany(userId, companyId).orElse(0L);
        }

        return BigDecimal.valueOf(currentCompanyJobExperience + employmentHistoryRepository.getExperienceAtCompany(userId, companyId).orElse(0L));
    }
}
