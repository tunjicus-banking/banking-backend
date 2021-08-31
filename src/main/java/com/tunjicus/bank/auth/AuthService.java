package com.tunjicus.bank.auth;

import com.tunjicus.bank.auth.dtos.GetLoginDto;
import com.tunjicus.bank.auth.dtos.PostLoginDto;
import com.tunjicus.bank.security.JwtProvider;
import com.tunjicus.bank.users.User;
import com.tunjicus.bank.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final IAuthenticationFacade authenticationFacade;

    GetLoginDto authenticateUser(PostLoginDto dto) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user =
                userRepository
                        .findByUsername(dto.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(dto.getUsername()));
        return new GetLoginDto(user, jwtProvider.generateToken(authentication));
    }

    public User getCurrentUser() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(
                        () ->
                                new UsernameNotFoundException(
                                        "Failed to find account for user: "
                                                + authentication.getName()));
    }
}
