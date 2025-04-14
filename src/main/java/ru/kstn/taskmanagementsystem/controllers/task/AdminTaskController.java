package ru.kstn.taskmanagementsystem.controllers.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kstn.taskmanagementsystem.dtos.task.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.services.task.TaskService;

@RestController
@RequestMapping("api/v1/admin/tasks")
@RequiredArgsConstructor
@Tag(name = "Admin task API", description = "Operations related to task management")
public class AdminTaskController {

    private final TaskService taskService;

    @Operation(summary = "Get task by ID", description = "Retrieves a task by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseTaskDto> getTaskById(
            @Parameter(description = "ID of the task", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Operation(summary = "Delete a task", description = "Deletes the task identified by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
