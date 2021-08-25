package com.tunjicus.bank.requirements.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "requirements_info")
@IdClass(RequirementInfoId.class)
@Getter
@Setter
@NoArgsConstructor
public class RequirementInfo {
    @Id
    @Column(name = "requirement_id", insertable = false, updatable = false)
    private int id;

    @Id
    @Column
    private String requirement;

    @Column
    private String specification;

    static Set<RequirementInfo> fromDto(int id, PostRequirementDto.PostRequirementInfoDto dto) {
        Set<RequirementInfo> infoSet = new HashSet<>();
        infoSet.add(new RequirementInfo(id, RequirementEnum.EXPERIENCE, dto.getYearsExperience()));
        infoSet.add(new RequirementInfo(id, RequirementEnum.COMPANY_EXPERIENCE, dto.getYearsOfCompanyExperience()));
        infoSet.add(new RequirementInfo(id, RequirementEnum.NET_WORTH, dto.getRequiredNetWorth()));
        infoSet.add(new RequirementInfo(id, RequirementEnum.CURRENT_SALARY, dto.getRequiredCurrentSalary()));
        return infoSet;
    }

    private RequirementInfo(int id, RequirementEnum requirementEnum, RequirementSpecification specification) {
        this.id = id;
        requirement = requirementEnum.label;
        this.specification = specification.toString();
    }
}
