package com.tunjicus.bank.accounts.enums;

public enum AccountType {
    CHECKING("C"),
    SAVINGS("S"),
    BRAND("B");

    public final String label;

    AccountType(String label) {
        this.label = label;
    }
}