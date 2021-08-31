package com.tunjicus.bank.positions;

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

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/position", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Positions", description = "Companies have positions")
public class PositionController {
    private final PositionService positionService;

    @Operation(
            summary = "Gets the positions (paginated)",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successful response"),
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
                        description = "Company not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public Page<Position> getAll(
            @Parameter(
                            description =
                                    "The company id to find positions for. Leave blank or set to negative number to find all")
                    @RequestParam(defaultValue = "-1")
                    int companyId,
            @Parameter(description = "The page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size") @RequestParam(defaultValue = "20") int size) {
        return positionService.findAll(companyId, page, size);
    }

    @Operation(
            summary = "Gets the position with the id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successful response"),
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
                        description = "Position not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public Position get(@Parameter(description = "The position id") @PathVariable int id) {
        return positionService.findById(id);
    }

    @Operation(
            summary = "Creates a position",
            responses = {
                @ApiResponse(responseCode = "201", description = "Position created"),
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
                        description = "Position not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Position create(@Valid @RequestBody Position position) {
        return positionService.create(position);
    }

    @Operation(
            summary = "Updates a position",
            responses = {
                @ApiResponse(responseCode = "200", description = "Position updated"),
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
                        description = "Position not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PutMapping("/{id}")
    public Position update(
            @Parameter(description = "The position id") @PathVariable int id,
            @Valid @RequestBody Position position) {
        return positionService.update(position, id);
    }

    @Operation(
            summary = "Deletes a position",
            responses = {
                @ApiResponse(responseCode = "204", description = "Position deleted"),
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
                        description = "Position not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "The position id to delete") @PathVariable int id) {
        positionService.delete(id);
    }
}
