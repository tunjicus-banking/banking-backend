package com.tunjicus.bank.transactions.exceptions;

public class SelfTransferException extends RuntimeException {
    public SelfTransferException(String message) {
        super(message);
    }
}
