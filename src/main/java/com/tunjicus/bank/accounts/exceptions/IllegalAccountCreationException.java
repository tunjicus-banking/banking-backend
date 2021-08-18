package com.tunjicus.bank.accounts.exceptions;

public class IllegalAccountCreationException extends RuntimeException {
    public IllegalAccountCreationException() {
        super("Cannot create account of this type");
    }
}
