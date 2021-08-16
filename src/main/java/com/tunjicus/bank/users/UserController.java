package com.tunjicus.bank.users;

import com.tunjicus.bank.shared.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Bank users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Gets an individual user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User has been found",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = User.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "User has not been found",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                        })
            })
    @GetMapping("/{id}")
    public User get(
            @Parameter(required = true, description = "The id of the user") @PathVariable int id) {
        return userService.findById(id);
    }

    @Operation(
            summary = "Creates a user",
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "User has been created",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = User.class)))
            })
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
                        description = "Search was run without any errors",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        array =
                                                @ArraySchema(
                                                        schema =
                                                                @Schema(
                                                                        implementation =
                                                                                User.class))))
            })
    @GetMapping("/find")
    public List<User> getByName(
            @Parameter(required = true, description = "The name to search for")
                    @RequestParam("name")
                    String name) {
        return userService.findByName(name);
    }
}
