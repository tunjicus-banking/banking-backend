package com.tunjicus.bank.offers.exceptions;

public class InvalidOfferUserException extends RuntimeException {
    public InvalidOfferUserException(long offerId, int userId) {
        super(String.format("Invalid user '%d' for offer '%d'", userId, offerId));
    }
}
