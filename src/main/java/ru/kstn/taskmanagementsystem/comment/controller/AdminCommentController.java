package ru.kstn.taskmanagementsystem.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kstn.taskmanagementsystem.comment.dto.RequestCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/comments")
@RequiredArgsConstructor
@Tag(name = "Admin comment API", description = "Operations related to comment management")
public class AdminCommentController {

    private final CommentService commentService;

    @Operation(summary = "Get all comments", description = "Retrieves a list of all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of comments")
    })
    @GetMapping
    public ResponseEntity<List<ResponseCommentDto>> getCommentList() {
        return ResponseEntity.ok(commentService.getCommentList());
    }


    @Operation(summary = "Get comments for a task", description = "Retrieves all comments associated with a specific task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comments for the task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}/task")
    public ResponseEntity<List<SimpleCommentDto>> getCommentsByTaskId(
            @Parameter(description = "ID of the task", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(id));
    }

    @Operation(summary = "Get comment by ID", description = "Retrieves a comment by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the comment"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseCommentDto> getCommentById(
            @Parameter(description = "ID of the comment", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @Operation(summary = "Update a comment", description = "Updates an existing comment identified by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid comment data supplied"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseCommentDto> updateComment(
            @Parameter(description = "ID of the comment to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Request DTO with updated comment details", required = true)
            @Valid @RequestBody RequestCommentDto request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @Operation(summary = "Delete a comment", description = "Deletes the comment identified by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID of the comment to delete", required = true)
            @PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
