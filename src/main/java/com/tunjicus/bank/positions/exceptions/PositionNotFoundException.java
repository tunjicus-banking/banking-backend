package com.tunjicus.bank.positions.exceptions;

public class PositionNotFoundException extends RuntimeException {
    public PositionNotFoundException(int id) {
        super(String.format("Failed to find position with id: %d", id));
    }
}
