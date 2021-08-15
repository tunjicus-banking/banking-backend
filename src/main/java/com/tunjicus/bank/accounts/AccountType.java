package com.tunjicus.bank.accounts;

public enum AccountType {
    CHECKING("C"),
    SAVINGS("S"),
    UNKNOWN("U");

    public final String label;

    AccountType(String label) {
        this.label = label;
    }
}