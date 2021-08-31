package com.tunjicus.bank.auth;

import com.tunjicus.bank.auth.dtos.GetLoginDto;
import com.tunjicus.bank.auth.dtos.PostLoginDto;
import com.tunjicus.bank.auth.dtos.RegisterDto;
import com.tunjicus.bank.exceptions.ErrorResponse;
import com.tunjicus.bank.users.UserService;
import com.tunjicus.bank.users.dtos.GetUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Login/create a user")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(
            summary = "Login to application as a user",
            responses = {
                @ApiResponse(responseCode = "200", description = "Login successful"),
                @ApiResponse(responseCode = "400", description = "Validation error"),
                @ApiResponse(
                        responseCode = "401",
                        description = "Bad credentials",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @PostMapping("/login")
    public GetLoginDto authenticate(@Valid @RequestBody PostLoginDto dto) {
        return authService.authenticateUser(dto);
    }

    @Operation(
            summary = "Create a new user account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Registration successful"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public GetUserDto create(@Valid @RequestBody RegisterDto dto) {
        return userService.save(dto);
    }
}
