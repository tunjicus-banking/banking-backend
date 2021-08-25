package com.tunjicus.bank.offers.exceptions;

public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(long id) {
        super(String.format("Failed to find offer with id: %d", id));
    }
}
