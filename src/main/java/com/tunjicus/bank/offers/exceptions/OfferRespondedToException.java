package com.tunjicus.bank.offers.exceptions;

public class OfferRespondedToException extends RuntimeException {
    public OfferRespondedToException(long offerId) {
        super(String.format("The offer '%d' has already been responded to", offerId));
    }
}
