package com.tunjicus.bank.companies;

import com.tunjicus.bank.companies.dto.GetCompanyDto;
import com.tunjicus.bank.shared.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Companies can employ users")
public class CompanyController {
    private final CompanyService companyService;

    @Operation(
            summary = "Gets all of the companies (paginated)",
            responses = {@ApiResponse(responseCode = "200", description = "Successful response")})
    @GetMapping
    public Page<GetCompanyDto> getAll(
            @Parameter(description = "The page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size") @RequestParam(defaultValue = "20") int size) {
        return companyService.findAll(page, size);
    }

    @Operation(
            summary = "Gets the company with the id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Company was found"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Company was not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public GetCompanyDto get(
            @Parameter(description = "The id of the company") @PathVariable int id) {
        return companyService.findById(id);
    }
}
