package com.tunjicus.bank.shared;

import java.util.Date;

public record ErrorResponse(Date timestamp, int status, String error, String message, String path) {}
