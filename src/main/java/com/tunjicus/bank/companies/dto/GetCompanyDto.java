package com.tunjicus.bank.companies.dto;

import com.tunjicus.bank.companies.Company;
import lombok.Getter;

@Getter
public class GetCompanyDto {
    private final int id;
    private final String name;
    private final String description;
    private final String brandColor;

    public GetCompanyDto(Company company) {
        id = company.getId();
        name = company.getName();
        description = company.getDescription();
        brandColor = String.format("#%06X", company.getBrandColor());
    }
}
