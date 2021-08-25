package com.tunjicus.bank.requirements.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "requirements")
@Getter
@Setter
@NoArgsConstructor
public class Requirement {
    @Id
    @Column(name = "requirement_id")
    private int id;

    @Column
    private double experience;

    @Column
    private double companyExperience;

    @Column
    private BigDecimal netWorth;

    @Column
    private BigDecimal currentSalary;

    @Column
    private byte modifier;

    @Column
    private byte requiredCompletion;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<RequirementInfo> info;

    public Requirement(PostRequirementDto dto) {
        id = dto.getJobPostingId();
        experience = dto.getYearsExperience();
        companyExperience = dto.getYearsOfCompanyExperience();
        netWorth = dto.getRequiredNetWorth();
        currentSalary = dto.getRequiredCurrentSalary();
        modifier = (byte) dto.getSalaryModifier();
        requiredCompletion = (byte) dto.getRequiredCompletionPercentage();
        info = RequirementInfo.fromDto(dto.getJobPostingId(), dto.getInfo());
    }
}
