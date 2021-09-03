package com.tunjicus.bank.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tunjicus.bank.auth.dtos.RegisterDto;
import com.tunjicus.bank.companies.dtos.PostCompanyDto;
import com.tunjicus.bank.roles.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @Column(unique = true)
    @NotBlank(message = "username cannot be blank")
    @Nationalized
    private String username;

    @Column
    @NotNull(message = "password cannot be null")
    private String password;

    @Column
    @NotBlank(message = "first name cannot be blank")
    @Nationalized
    private String firstName;

    @Column
    @NotBlank(message = "last name cannot be blank")
    @Nationalized
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(RegisterDto dto, String hashedPass) {
        username = dto.getUsername();
        password = hashedPass;
        firstName = dto.getFirstName();
        lastName = dto.getLastName();
        roles = new HashSet<>();
    }

    public User(PostCompanyDto dto) {
        username = dto.getName();
        password = "";
        var names = dto.getName().split(" ");
        firstName = names[0];

        if (names.length > 1) {
            lastName = Strings.join(Arrays.asList(Arrays.copyOfRange(names, 1, names.length)), ' ');
        } else {
            lastName = "Co";
        }
    }
}
