package com.tunjicus.bank.requirements.models;

import lombok.Getter;

@Getter
public class GetRequirementDto {
    private final double experience;
    private final double companyExperience;

    public GetRequirementDto(Requirement requirement) {
        experience = requirement.getExperience();
        companyExperience = requirement.getCompanyExperience();
    }
}
