package ru.kstn.taskmanagementsystem.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
@Schema(description = "Request DTO for comment")
public class RequestCommentDto {

    @NotBlank(message = "Content cannot be empty")
    @Schema(description = "The content of the comment", example = "content example")
    private String content;

    @NotNull(message = "Creator id cannot be a null")
    @Positive(message = "Creator id must be at least 1")
    @Schema(description = "The creator id of the comment", example = "1")
    private Long creatorId;

    @NotNull(message = "Task id cannot be a null")
    @Positive(message = "Task id must be at least 1")
    @Schema(description = "The task id of the comment", example = "1")
    private Long taskId;
}
