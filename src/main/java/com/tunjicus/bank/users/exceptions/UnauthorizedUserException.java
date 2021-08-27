package com.tunjicus.bank.users.exceptions;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super("You do not have permission to perform this action");
    }
}
