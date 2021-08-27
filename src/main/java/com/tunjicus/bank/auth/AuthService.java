package com.tunjicus.bank.auth;

import com.tunjicus.bank.auth.dtos.GetLoginDto;
import com.tunjicus.bank.auth.dtos.JwtResponse;
import com.tunjicus.bank.auth.dtos.PostLoginDto;
import com.tunjicus.bank.security.JwtProvider;
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
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    GetLoginDto authenticateUser(PostLoginDto dto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new UsernameNotFoundException(dto.getUsername()));
        return new GetLoginDto(user, jwtProvider.generateToken(authentication));
    }
}
