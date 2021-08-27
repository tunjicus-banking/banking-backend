package com.tunjicus.bank.users.dtos;

import com.tunjicus.bank.users.User;
import lombok.Getter;

@Getter
public class GetUserDto {
    private final int id;
    private final String username;
    private final String firstName;
    private final String lastName;

    public GetUserDto(User user) {
        id = user.getId();
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
    }
}
