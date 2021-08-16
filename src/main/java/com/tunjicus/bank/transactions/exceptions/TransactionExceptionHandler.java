package com.tunjicus.bank.transactions.exceptions;

import com.tunjicus.bank.shared.ErrorResponse;
import com.tunjicus.bank.transactions.exceptions.InsufficientFundsException;
import com.tunjicus.bank.transactions.exceptions.NoCheckingAccountException;
import com.tunjicus.bank.transactions.exceptions.SelfTransferException;
import com.tunjicus.bank.transactions.exceptions.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
        SelfTransferException.class,
        InsufficientFundsException.class,
        NoCheckingAccountException.class,
        TransactionException.class
    })
    public ResponseEntity<ErrorResponse> handleTransactionExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage()));
    }
}
