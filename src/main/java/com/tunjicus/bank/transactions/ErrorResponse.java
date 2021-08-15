package com.tunjicus.bank.transactions;

import org.springframework.http.HttpStatus;

record ErrorResponse(HttpStatus code, String message) {}
