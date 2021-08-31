package com.tunjicus.bank.items.exceptions;

public class ItemExistsException extends RuntimeException {
    public ItemExistsException(String name) {
        super(String.format("An item with the name '%s' already exists", name));
    }
}
