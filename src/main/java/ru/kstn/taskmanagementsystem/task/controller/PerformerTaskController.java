package ru.kstn.taskmanagementsystem.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kstn.taskmanagementsystem.task.dto.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.enums.TaskStatus;
import ru.kstn.taskmanagementsystem.task.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/v1/performer/tasks")
@RequiredArgsConstructor
@Tag(name = "Performer task API", description = "Operations related to task management")
public class PerformerTaskController {

    private final TaskService taskService;

    @Operation(summary = "Get tasks by performer username", description = "Retrieves a paginated list of tasks assigned to the specified performer username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied")
    })
    @GetMapping("/performers")
    public ResponseEntity<List<SimpleTaskDto>> getTaskListByPerformerUsername(
            @Parameter(description = "Page number (default 0)", schema = @Schema(defaultValue = "0"))
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (default 10)", schema = @Schema(defaultValue = "10"))
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Username of the performer", required = true)
            @RequestParam String username
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok(taskService.getTaskListByPerformerUsername(username, pageable));
    }

    @Operation(summary = "Change task status", description = "Changes the status of the specified task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseTaskDto> changeStatus(
            @Parameter(description = "ID of the task", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status for the task", required = true)
            @RequestBody TaskStatus status) {
        return ResponseEntity.ok(taskService.changeStatus(id, status));
    }
}
