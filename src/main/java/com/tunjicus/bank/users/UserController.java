package com.tunjicus.bank.users;

import com.tunjicus.bank.shared.ErrorResponse;
import com.tunjicus.bank.shared.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Bank users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Gets an individual user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User has been found",
                        content =
                                @Content(
                                        mediaType = MediaType.JSON,
                                        schema = @Schema(implementation = User.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "User has not been found",
                        content = {
                            @Content(
                                    mediaType = MediaType.JSON,
                                    schema = @Schema(implementation = ErrorResponse.class))
                        })
            })
    public User get(
            @Parameter(required = true, description = "The id of the user") @PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/find")
    public ArrayList<User> getByName(@RequestParam("name") String name) {
        return userService.findByName(name);
    }
}
