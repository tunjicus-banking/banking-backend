package com.tunjicus.bank.exceptions;

import com.tunjicus.bank.accounts.exceptions.AccountNotFoundException;
import com.tunjicus.bank.accounts.exceptions.IllegalAccountCreationException;
import com.tunjicus.bank.accounts.exceptions.ValidAccountNotFoundException;
import com.tunjicus.bank.banks.exceptions.BankNotFoundException;
import com.tunjicus.bank.companies.exceptions.CompanyExistsException;
import com.tunjicus.bank.companies.exceptions.CompanyNotFoundException;
import com.tunjicus.bank.items.exceptions.ItemExistsException;
import com.tunjicus.bank.items.exceptions.ItemNotFoundException;
import com.tunjicus.bank.jobPostings.exceptions.CompanyApplicationException;
import com.tunjicus.bank.jobPostings.exceptions.InvalidDuplicateApplicationException;
import com.tunjicus.bank.jobPostings.exceptions.JobPostingNotFoundException;
import com.tunjicus.bank.offers.exceptions.InvalidOfferUserException;
import com.tunjicus.bank.offers.exceptions.OfferNotFoundException;
import com.tunjicus.bank.offers.exceptions.OfferRespondedToException;
import com.tunjicus.bank.positions.exceptions.PositionNotFoundException;
import com.tunjicus.bank.transactions.exceptions.InsufficientFundsException;
import com.tunjicus.bank.transactions.exceptions.NoCheckingAccountException;
import com.tunjicus.bank.transactions.exceptions.SelfTransferException;
import com.tunjicus.bank.transactions.exceptions.TransactionException;
import com.tunjicus.bank.users.exceptions.UnauthorizedUserException;
import com.tunjicus.bank.users.exceptions.UserNotFoundException;
import com.tunjicus.bank.users.exceptions.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    @ExceptionHandler({
        OfferNotFoundException.class,
        AccountNotFoundException.class,
        BankNotFoundException.class,
        CompanyNotFoundException.class,
        JobPostingNotFoundException.class,
        PositionNotFoundException.class,
        UserNotFoundException.class,
        ItemNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e) {
        return handleExceptions(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({InvalidOfferUserException.class, UnauthorizedUserException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(Exception e) {
        return handleExceptions(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler({
        OfferRespondedToException.class,
        IllegalAccountCreationException.class,
        ValidAccountNotFoundException.class,
        CompanyExistsException.class,
        CompanyApplicationException.class,
        InvalidDuplicateApplicationException.class,
        SelfTransferException.class,
        InsufficientFundsException.class,
        NoCheckingAccountException.class,
        TransactionException.class,
        UserValidationException.class,
        ItemExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        return handleExceptions(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity<ErrorResponse> handleExceptions(HttpStatus httpStatus, Exception e) {
        return ResponseEntity.status(httpStatus)
                .body(
                        new ErrorResponse(
                                new Date(),
                                httpStatus.value(),
                                httpStatus.name(),
                                e.getLocalizedMessage(),
                                request.getRequestURI()));
    }
}
