package com.tunjicus.bank.jobPostings.exceptions;

public class JobPostingNotFoundException extends RuntimeException {
    public JobPostingNotFoundException(int id) {
        super(String.format("Failed to find job posting with id: %d", id));
    }
}
