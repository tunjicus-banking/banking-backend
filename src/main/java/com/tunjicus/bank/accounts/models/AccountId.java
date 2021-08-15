package com.tunjicus.bank.accounts.models;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@RequiredArgsConstructor
public class AccountId implements Serializable {
    private int id;
    private String type;
}
