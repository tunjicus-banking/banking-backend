package com.tunjicus.bank.items;

import com.tunjicus.bank.exceptions.ErrorResponse;
import com.tunjicus.bank.items.dtos.GetItemDto;
import com.tunjicus.bank.items.dtos.PostItemDto;
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
@RequestMapping(value = "/item", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Items", description = "Represents an item that users can sell/buy")
public class ItemController {
    private final ItemService itemService;

    @Operation(
            summary = "Gets all items. Filterable by name and user id (paginated)",
            responses = {
                @ApiResponse(responseCode = "200", description = "Response has been generated"),
                @ApiResponse(
                        responseCode = "401",
                        description = "You need to be logged in to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "You don't have permission to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public Page<GetItemDto> getAll(
            @Parameter(description = "The name to search for") @RequestParam(defaultValue = "") String name,
            @Parameter(description = "A user id to filter by") @RequestParam(defaultValue = "-1")
                    int userId,
            @Parameter(description = "The page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size") @RequestParam(defaultValue = "20") int size) {
        return itemService.findAll(name, userId, page, size);
    }

    @Operation(
            summary = "Gets an item by id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Item has been found"),
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
                        description = "Failed to find item with this id",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public GetItemDto get(@Parameter(description = "The id of the item") @PathVariable int id) {
        return itemService.findById(id);
    }

    @Operation(
            summary = "Creates an item",
            responses = {
                @ApiResponse(responseCode = "201", description = "Item has been created"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Validation error or an item with this name already exists",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "You need to be logged in to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "You don't have permission to perform this action",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetItemDto create(@Valid @RequestBody PostItemDto dto) {
        return itemService.create(dto);
    }

    @Operation(
            summary = "Deletes an item",
            responses = {
                @ApiResponse(responseCode = "200", description = "Item has been deleted"),
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
                        description = "Item has not been found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "The id of the item") @PathVariable int id) {
        itemService.delete(id);
    }
}
