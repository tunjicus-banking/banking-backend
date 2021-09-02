package com.tunjicus.bank.offers;

import com.tunjicus.bank.scheduled.TimeService;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class GetOfferDto {
    private final long id;
    private final int jobPostingId;
    private final int userId;
    private final OfferStatus status;
    private final BigDecimal salary;
    private final Date offerTime;

    public GetOfferDto(Offer offer) {
        id = offer.getId();
        jobPostingId = offer.getJobPostingId();
        userId = offer.getUserId();
        salary = offer.getSalary();
        status = toOfferStatus(offer.getAccepted());
        offerTime = offer.getOfferTime() == null ? new Date() : TimeService.calculateSimulatedDate(offer.getOfferTime());
    }

    private static OfferStatus toOfferStatus(int status) {
        return switch (status) {
            case -1 -> OfferStatus.REJECTED;
            case 0 -> OfferStatus.OFFERED;
            case 1 -> OfferStatus.ACCEPTED;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }
}
