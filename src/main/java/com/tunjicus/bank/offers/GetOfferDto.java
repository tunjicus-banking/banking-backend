package com.tunjicus.bank.offers;

import com.tunjicus.bank.offers.Offer;
import lombok.Getter;

import java.util.Date;

@Getter
public class GetOfferDto {
    private final long id;
    private final int jobPostingId;
    private final int userId;
    private final offerStatus status;
    private final Date offerTime;

    enum offerStatus {ACCEPTED, REJECTED, OFFERED}

    public GetOfferDto(Offer offer) {
        id = offer.getId();
        jobPostingId = offer.getJobPostingId();
        userId = offer.getUserId();
        status = toOfferStatus(offer.getAccepted());
        offerTime = new Date();
    }

    private static offerStatus toOfferStatus(int status) {
        return switch (status) {
            case -1 -> offerStatus.REJECTED;
            case 0 -> offerStatus.OFFERED;
            case 1 -> offerStatus.ACCEPTED;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }
}
