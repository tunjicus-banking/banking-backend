package com.tunjicus.bank.transactions;

public record TransactionAccountInfo(int fromAccount, String fromType, int toAccount, String toType) {}
