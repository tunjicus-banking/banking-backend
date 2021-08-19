package com.tunjicus.bank.accounts;

import com.tunjicus.bank.accounts.dtos.GetAccountDto;
import com.tunjicus.bank.accounts.dtos.PostAccountDto;
import com.tunjicus.bank.shared.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Operations for bank accounts")
public class AccountController {
    private final AccountService accountService;

    @Operation(
            summary = "Gets an individual account",
            responses = {
                @ApiResponse(responseCode = "200", description = "Account has been found"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Account has not been found (closed or doesn't exist)",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public GetAccountDto get(
            @Parameter(description = "The id of the account") @PathVariable int id) {
        return accountService.findById(id);
    }

    @Operation(
            summary = "Creates a bank account for a user",
            responses = {
                @ApiResponse(responseCode = "201", description = "Account successfully created"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Attempted to create an account of type unknown",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Failed to find the user the account was intended for",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetAccountDto create(@Valid @RequestBody PostAccountDto accountDto) {
        return accountService.save(accountDto);
    }

    @Operation(
            summary = "Deletes an account",
            responses = {
                @ApiResponse(responseCode = "204", description = "The account has been deleted"),
                @ApiResponse(
                        responseCode = "404",
                        description = "The account cannot be found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "The id of the account") @PathVariable int id) {
        accountService.delete(id);
    }
}
