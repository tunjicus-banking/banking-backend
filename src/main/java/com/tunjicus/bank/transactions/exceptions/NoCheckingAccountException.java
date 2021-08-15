package com.tunjicus.bank.transactions.exceptions;

public class NoCheckingAccountException extends RuntimeException {
    public NoCheckingAccountException(String message) {
        super(message);
    }
}
