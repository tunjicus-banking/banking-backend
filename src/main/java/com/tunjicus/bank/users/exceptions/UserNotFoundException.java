package com.tunjicus.bank.users.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super(String.format("Failed to find user with id: %d", id));
    }
}
