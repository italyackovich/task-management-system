package ru.kstn.taskmanagementsystem.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kstn.taskmanagementsystem.comment.dto.RequestCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.comment.service.CommentService;

@RestController
@RequestMapping("/api/v1/performer/comments")
@RequiredArgsConstructor
@Tag(name = "Performer comment API", description = "Operations related to create comment")
public class PerformerCommentController {

    private final CommentService commentService;

    @Operation(summary = "Create a comment", description = "Creates a new comment with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid comment data supplied")
    })
    @PostMapping
    public ResponseEntity<ResponseCommentDto> createComment(
            @Parameter(description = "Request DTO for creating a comment", required = true)
            @Valid @RequestBody RequestCommentDto request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }
}
