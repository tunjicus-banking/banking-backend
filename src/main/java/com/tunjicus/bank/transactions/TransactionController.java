package com.tunjicus.bank.transactions;

import com.tunjicus.bank.shared.ErrorResponse;
import com.tunjicus.bank.transactions.dtos.GetTransactionDto;
import com.tunjicus.bank.transactions.dtos.PostTransactionDto;
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
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Defines a transaction between two users")
public class TransactionController {
    private final TransactionService transactionService;

    @Operation(
            summary = "Creates a transaction between two users",
            description =
                    "Makes sure that each user exists, the sending user has the funds, and that both users have checking accounts",
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "The transaction was successful",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema =
                                                @Schema(implementation = GetTransactionDto.class))),
                @ApiResponse(
                        responseCode = "400",
                        description =
                                "The request failed for some reason, i.e. the sending user didn't have the funds",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetTransactionDto create(@Valid @RequestBody PostTransactionDto transactionDto) {
        return transactionService.usersTransfer(transactionDto);
    }
}
