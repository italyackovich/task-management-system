package ru.kstn.taskmanagementsystem.dtos.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.kstn.taskmanagementsystem.model.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.model.enums.TaskStatus;

@Data
@Schema(description = "Request DTO for task")
public class RequestTaskDto {

    @NotBlank(message = "Title cannot be blank")
    @Schema(description = "The title of the task", example = "Title example")
    private String title;

    @Schema(description = "The description of the task", example = "Description example", nullable = true)
    private String description;

    @NotNull(message = "Status cannot be a null")
    @Schema(description = "The status of the task", example = "AWAITING")
    private TaskStatus status;

    @NotNull(message = "Priority cannot be a null")
    @Schema(description = "The priority of the task", example = "HIGH")
    private TaskPriority priority;

    @NotNull(message = "Creator id cannot be a null")
    @Positive(message = "Creator id must be at least 1")
    @Schema(description = "The creator id of the task", example = "1")
    private Long creatorId;

    @NotNull(message = "Performer id cannot be a null")
    @Positive(message = "Performer id must be at least 1")
    @Schema(description = "The performer id of the task", example = "2")
    private Long performerId;
}
