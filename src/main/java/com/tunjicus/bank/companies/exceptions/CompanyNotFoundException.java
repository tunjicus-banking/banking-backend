package com.tunjicus.bank.companies.exceptions;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(int id) {
        super(String.format("Failed to find company with id: %d", id));
    }
}
