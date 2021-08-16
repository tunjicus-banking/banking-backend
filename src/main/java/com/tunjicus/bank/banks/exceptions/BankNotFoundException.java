package com.tunjicus.bank.banks.exceptions;

public class BankNotFoundException extends RuntimeException {
    public BankNotFoundException(int id) {
        super(String.format("Failed to find bank with id: %d", id));
    }
}
