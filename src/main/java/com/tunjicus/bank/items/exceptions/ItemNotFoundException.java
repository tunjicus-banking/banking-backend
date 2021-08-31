package com.tunjicus.bank.items.exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(int id) {
        super(String.format("Failed to find item with id: %d", id));
    }
}
