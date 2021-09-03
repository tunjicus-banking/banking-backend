package com.tunjicus.bank.companies;

import com.tunjicus.bank.companies.dtos.PostCompanyDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "companies")
@Getter
@Setter
@NoArgsConstructor
public class Company {
    @Id
    @Column(name = "company_id")
    private int id;

    @Column(length = 500)
    @Nationalized
    private String name;

    @Column(length = 1000)
    @Nationalized
    private String description;

    @Column
    private int brandColor;

    public Company(PostCompanyDto dto, int id) {
        this.id = id;
        name = dto.getName();
        description = dto.getDescription();
        brandColor = dto.getBrandColor();
    }
}
