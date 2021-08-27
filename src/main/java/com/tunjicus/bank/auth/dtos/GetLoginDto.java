package com.tunjicus.bank.auth.dtos;

import com.tunjicus.bank.users.User;
import com.tunjicus.bank.users.dtos.GetUserDto;
import lombok.Getter;

@Getter
public class GetLoginDto extends GetUserDto {
    private final String token;

    public GetLoginDto(User user, String token) {
        super(user);
        this.token = token;
    }
}
