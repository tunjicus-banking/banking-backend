package com.tunjicus.bank.jobPostings.exceptions;

public class InvalidDuplicateApplicationException extends RuntimeException {
    public InvalidDuplicateApplicationException() {
        super("Cannot apply to a position you currently have or to one you have an open offer with");
    }
}
