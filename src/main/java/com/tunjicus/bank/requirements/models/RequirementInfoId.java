package com.tunjicus.bank.requirements.models;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@RequiredArgsConstructor
public class RequirementInfoId implements Serializable {
    private int id;
    private String requirement;
}
