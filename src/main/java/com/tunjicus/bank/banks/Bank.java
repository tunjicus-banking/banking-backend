package com.tunjicus.bank.banks;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity(name = "banks")
@Getter
@Setter
@ToString
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @Column
    @NotBlank(message = "name cannot be empty")
    @Nationalized
    private String name;

    @Column
    @NotBlank(message = "location cannot be empty")
    @Nationalized
    private String location;

    @Column
    @NotNull
    @Positive
    @NumberFormat(pattern = "%.2f%n")
    @DecimalMin(message = "Bank needs at least $10,000 to start", value = "10000")
    private BigDecimal funds;
}
