package com.tunjicus.bank.positions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity(name = "positions")
@Getter
@Setter
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private int id;

    @Column
    private int companyId;

    @Column
    @Nationalized
    private String title;

    @Column
    @Nationalized
    private String description;

    @Column
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean active;
}
