package com.tunjicus.bank.jobPostings.exceptions;

import com.tunjicus.bank.companies.exceptions.CompanyNotFoundException;
import com.tunjicus.bank.shared.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JobPostingExceptionHandler {

    @ExceptionHandler(JobPostingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleJobPostingExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getLocalizedMessage()));
    }
}
