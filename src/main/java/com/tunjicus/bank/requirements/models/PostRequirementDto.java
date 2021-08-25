package com.tunjicus.bank.requirements.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class PostRequirementDto {
    @NotNull private int jobPostingId;
    private double yearsExperience;
    private double yearsOfCompanyExperience;
    private BigDecimal requiredNetWorth = BigDecimal.ZERO;
    private BigDecimal requiredCurrentSalary = BigDecimal.ZERO;

    @NotNull
    @Range(min = 0, max = 100, message = "Salary modifier must been between 0 & 100 (inclusive)")
    private int salaryModifier;

    @NotNull
    @Range(
            min = 1,
            max = 100,
            message = "Completion percentage must been between 1 & 100 (inclusive)")
    @Schema(defaultValue = "100")
    private int requiredCompletionPercentage;

    @NotNull private PostRequirementInfoDto info;

    @Getter
    @Setter
    static class PostRequirementInfoDto {
        private RequirementSpecification yearsExperience;
        private RequirementSpecification yearsOfCompanyExperience;
        private RequirementSpecification requiredNetWorth;
        private RequirementSpecification requiredCurrentSalary;
    }
}
