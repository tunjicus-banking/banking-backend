package com.tunjicus.bank.shared;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus code, String message) {}
