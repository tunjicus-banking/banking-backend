package com.tunjicus.bank.offers;

import com.tunjicus.bank.exceptions.ErrorResponse;
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

@RestController
@RequestMapping(value = "/offer", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Offers", description = "Represents a job offer")
public class OfferController {
    private final OfferService offerService;

    @Operation(
            summary = "Gets all offers for the user (paginated)",
            responses = {
                @ApiResponse(responseCode = "200", description = "Result returned"),
                @ApiResponse(
                        responseCode = "401",
                        description = "You need to be logged in to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "You don't have permission to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "User with id not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public Page<GetOfferDto> getAll(
            @Parameter(description = "The id of the user") @RequestParam int userId,
            @Parameter(description = "The page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size") @RequestParam(defaultValue = "20") int size) {
        return offerService.findAllByUserId(userId, page, size);
    }

    @Operation(
            summary =
                    "Accepts an offer. Accepting an offer will cause the user to lose their current job",
            responses = {
                @ApiResponse(responseCode = "204", description = "Offer accepted"),
                @ApiResponse(
                        responseCode = "400",
                        description = "The offer has already been responded to",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "You need to be logged in to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "You don't have permission to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "An offer with this id was not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping("/accept/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void accept(
            @Parameter(description = "The id of the offer") @PathVariable long id) {
        offerService.acceptOffer(id);
    }

    @Operation(
            summary = "Rejects an offer",
            responses = {
                @ApiResponse(responseCode = "204", description = "Offer rejected"),
                @ApiResponse(
                        responseCode = "400",
                        description = "The offer has already been responded to",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "You need to be logged in to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "This user cannot modify this offer",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "An offer with this id was not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping("/reject/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(
            @Parameter(description = "The id of the offer") @PathVariable long id) {
        offerService.rejectOffer(id);
    }
}
