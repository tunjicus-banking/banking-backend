package com.tunjicus.bank.jobPostings.exceptions;

public class CompanyApplicationException extends RuntimeException {
    public CompanyApplicationException() {
        super("Companies cannot apply for jobs");
    }
}
