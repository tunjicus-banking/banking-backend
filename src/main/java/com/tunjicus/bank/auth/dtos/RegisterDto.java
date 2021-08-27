package com.tunjicus.bank.auth.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterDto {
    @NotBlank(message = "username cannot be null")
    private String username;

    @NotBlank(message = "password cannot be null")
    private String password;

    @NotBlank(message = "confirmPassword cannot be null")
    private String confirmPassword;

    @NotBlank(message = "first name cannot be null")
    private String firstName;

    @NotBlank(message = "last name cannot be null")
    private String lastName;
}
