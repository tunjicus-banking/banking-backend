package com.tunjicus.bank.accounts.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(int id) {
        super(String.format("Failed to find account with id: %d", id));
    }
}
