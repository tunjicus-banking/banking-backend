package com.tunjicus.bank.accounts.exceptions;

public class ValidAccountNotFoundException extends RuntimeException {
    public ValidAccountNotFoundException(int userId, int accountId) {
        super(
                String.format(
                        "Failed to find a valid account with id '%d' for user '%d'", accountId, userId));
    }
}
