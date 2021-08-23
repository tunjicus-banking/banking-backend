package com.tunjicus.bank.offers.exceptions;

import com.tunjicus.bank.shared.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OfferExceptionHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOfferNotFoundException(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getLocalizedMessage()));
    }

    @ExceptionHandler(OfferRespondedToException.class)
    public ResponseEntity<ErrorResponse> handleBadRequests(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage()));
    }

    @ExceptionHandler(InvalidOfferUserException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN, e.getLocalizedMessage()));
    }
}
