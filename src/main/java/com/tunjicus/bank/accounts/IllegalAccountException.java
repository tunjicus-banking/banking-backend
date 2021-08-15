package com.tunjicus.bank.accounts;

import lombok.ToString;

@ToString
public class IllegalAccountException extends Exception {
    private final String message;

    public IllegalAccountException(String message) {
        this.message = message;
    }
}
