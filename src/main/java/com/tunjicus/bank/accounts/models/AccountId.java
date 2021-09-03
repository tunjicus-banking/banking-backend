package com.tunjicus.bank.accounts.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class AccountId implements Serializable {
    private int id;
    private String type;
}
