package com.tunjicus.bank.users.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super(String.format("Failed to find user with id: %d", id));
    }
    public UserNotFoundException(String username) {
        super(String.format("Failed to find user with username: %s", username));
    }
}
