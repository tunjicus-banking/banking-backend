package com.tunjicus.bank.jobPostings;

import com.tunjicus.bank.jobPostings.dtos.GetJobPostingDto;
import com.tunjicus.bank.jobPostings.dtos.PostJobPostingDto;
import com.tunjicus.bank.shared.ErrorResponse;
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
@RequestMapping(value = "/posting", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Job Postings", description = "Represents a job posting for a position")
public class JobPostingController {
    private final JobPostingService jobPostingService;

    @Operation(
            summary = "Gets a job posting by id",
            responses = {
                @ApiResponse(responseCode = "200", description = "The posting was found"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Posting not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    public GetJobPostingDto get(
            @Parameter(description = "The id of the posting") @PathVariable int id) {
        return jobPostingService.findById(id);
    }

    @Operation(
            summary = "Gets all job postings (paginated)",
            responses = {
                @ApiResponse(responseCode = "200", description = "Result returned"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Company id or position id not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public Page<GetJobPostingDto> getAll(
            @Parameter(description = "The id of the company to filter by. This takes precedence over the position id if set")
                    @RequestParam(defaultValue = "-1")
                    int companyId,
            @Parameter(
                            description =
                                    "The id of the position to filter by")
                    @RequestParam(defaultValue = "-1")
                    int positionId,
            @Parameter(
                            description =
                                    "A switch to include all postings or just the active ones. Defaults to false (only the active ones)")
                    @RequestParam(defaultValue = "false")
                    boolean includeAll,
            @Parameter(description = "The page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "The page size") @RequestParam(defaultValue = "20") int size) {
        return jobPostingService.findAll(companyId, positionId, includeAll, page, size);
    }

    @Operation(
            summary = "Creates a job posting",
            responses = {
                @ApiResponse(responseCode = "201", description = "The posting was created"),
                @ApiResponse(
                        responseCode = "404",
                        description = "A valid position with the position id was not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetJobPostingDto create(@Valid @RequestBody PostJobPostingDto dto) {
        return jobPostingService.create(dto);
    }

    @Operation(
            summary = "Updates a job posting",
            responses = {
                @ApiResponse(responseCode = "200", description = "The posting was updated"),
                @ApiResponse(
                        responseCode = "404",
                        description =
                                "Either a valid position with the position id was not found or the posting id was not found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PutMapping("/{id}")
    public GetJobPostingDto update(
            @Parameter(description = "The id of the posting to update") @PathVariable int id,
            @Valid @RequestBody PostJobPostingDto dto) {
        return jobPostingService.update(dto, id);
    }

    @Operation(
            summary = "Deletes a job posting",
            responses = {
                @ApiResponse(responseCode = "200", description = "The posting was deleted"),
                @ApiResponse(
                        responseCode = "404",
                        description = "A posting with the id could not be found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "The id of the posting to delete") @PathVariable int id) {
        jobPostingService.delete(id);
    }
}
