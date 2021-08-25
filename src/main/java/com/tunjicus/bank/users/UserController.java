package com.tunjicus.bank.users;

import com.tunjicus.bank.employmentHistory.GetEmploymentHistoryDto;
import com.tunjicus.bank.shared.ErrorResponse;
import com.tunjicus.bank.transactions.TransactionService;
import com.tunjicus.bank.transactions.dtos.SelfTransferDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Users", description = "Bank users")
public class UserController {
    private final UserService userService;
    private final TransactionService transactionService;

    @Operation(
            summary = "Gets an individual user",
            responses = {
                @ApiResponse(responseCode = "200", description = "User has been found"),
                @ApiResponse(
                        responseCode = "404",
                        description = "User has not been found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public User get(@Parameter(description = "The id of the user") @PathVariable int id) {
        return userService.findById(id);
    }

    @Operation(
            summary = "Updates a user",
            responses = {
                @ApiResponse(responseCode = "200", description = "User has been updated"),
                @ApiResponse(
                        responseCode = "404",
                        description = "User has not been found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PutMapping("/{id}")
    public User update(
            @Parameter(description = "The id of the user") @PathVariable int id,
            @Valid @RequestBody User user) {
        return userService.update(user, id);
    }

    @Operation(
            summary = "Creates a user",
            responses = {@ApiResponse(responseCode = "201", description = "User has been created")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @Operation(
            summary = "Finds users based on name",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Search was run without any errors")
            })
    @GetMapping("/find")
    public Page<User> getByName(
            @Parameter(description = "The name to search for") @RequestParam String name,
            @Parameter(description = "The page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size") @RequestParam(defaultValue = "20") int size) {
        return userService.findByName(name, page, size);
    }

    @Operation(
            summary = "Make a transfer between a user's accounts",
            responses = {
                @ApiResponse(responseCode = "201", description = "Transfer was successful"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Failed to validate accounts for the user",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public SelfTransferDto transfer(@Valid @RequestBody SelfTransferDto dto) {
        return transactionService.selfTransfer(dto);
    }


    @Operation(
            summary = "Gets a user's employment history",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Failed to find user",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("employment/{id}")
    public Page<GetEmploymentHistoryDto> getEmploymentHistory(
            @Parameter(description = "The id of the user") @PathVariable int id,
            @Parameter(description = "The page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size") @RequestParam(defaultValue = "20") int size) {
        return userService.getEmploymentHistory(id, page, size);
    }
}
