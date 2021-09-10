package com.tunjicus.bank.items.exceptions;

public class SelfPurchaseException extends RuntimeException {
    public SelfPurchaseException() {
        super("You cannot purchase an item you have listed");
    }
}
