package com.tunjicus.bank.banks;

import com.tunjicus.bank.shared.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
@Tag(name = "Banks", description = "Crud operations for banks")
public class BankController {
    private final BankService bankService;

    @Operation(
            summary = "Gets all of the banks, not paginated",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful request",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        array =
                                                @ArraySchema(
                                                        schema =
                                                                @Schema(
                                                                        implementation =
                                                                                Bank.class))))
            })
    @GetMapping
    public List<Bank> getAll() {
        return bankService.findAll();
    }

    @Operation(
            summary = "Get an individual bank",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Bank was found",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = Bank.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Bank was not found",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public Bank get(@PathVariable int id) {
        return bankService.findById(id);
    }

    @Operation(
            summary = "Creates a bank",
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "The bank was created",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = Bank.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bank created(@Valid @RequestBody Bank bank) {
        return bankService.save(bank);
    }
}
