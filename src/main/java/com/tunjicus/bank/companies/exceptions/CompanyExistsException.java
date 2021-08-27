package com.tunjicus.bank.companies.exceptions;

public class CompanyExistsException extends RuntimeException {
    public CompanyExistsException(String name) {
        super(String.format("A company or user with the name %s already exists", name));
    }
}
