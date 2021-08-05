package com.tunjicus.bank.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @Column(unique = true)
    @Email
    @NotBlank(message = "email cannot be blank")
    private String email;

    @Column
    @NotBlank(message = "first name cannot be blank")
    private String firstName;

    @Column
    @NotBlank(message = "last name cannot be blank")
    private String lastName;
}
