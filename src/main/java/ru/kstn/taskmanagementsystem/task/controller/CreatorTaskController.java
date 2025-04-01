package ru.kstn.taskmanagementsystem.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kstn.taskmanagementsystem.task.dto.RequestTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.task.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/v1/creator/tasks")
@RequiredArgsConstructor
@Tag(name = "Creator task API", description = "Operations related to task management")
public class CreatorTaskController {

    private final TaskService taskService;

    @Operation(summary = "Get tasks by creator username", description = "Retrieves a paginated list of tasks created by the specified username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied")
    })
    @GetMapping("/creators")
    public ResponseEntity<List<SimpleTaskDto>> getTaskListByCreatorUsername(
            @Parameter(description = "Page number (default 0)", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default 10)", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Username of the creator", required = true)
            @RequestParam String username
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(taskService.getTaskListByCreatorUsername(username, pageable));
    }

    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid task details")
    })
    @PostMapping
    public ResponseEntity<ResponseTaskDto> createTask(
            @Parameter(description = "Task details", required = true)
            @Valid @RequestBody RequestTaskDto request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @Operation(summary = "Update a task", description = "Updates an existing task identified by its ID with new details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid task details"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseTaskDto> updateTask(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated task details", required = true)
            @Valid @RequestBody RequestTaskDto request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @Operation(summary = "Update task performers", description = "Updates the list of performers for the specified task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performers list updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/performers")
    public ResponseEntity<ResponseTaskDto> updatePerformersList(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID of the performer to add", required = true)
            @RequestBody Long performerId) {
        return ResponseEntity.ok(taskService.updatePerformerToList(id, performerId));
    }

    @Operation(summary = "Change task priority", description = "Changes the priority of the specified task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Priority updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/priority")
    public ResponseEntity<ResponseTaskDto> changePriority(
            @Parameter(description = "ID of the task", required = true)
            @PathVariable Long id,
            @Parameter(description = "New priority for the task", required = true)
            @RequestBody TaskPriority priority) {
        return ResponseEntity.ok(taskService.changePriority(id, priority));
    }
}
