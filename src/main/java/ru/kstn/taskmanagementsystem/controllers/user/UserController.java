package ru.kstn.taskmanagementsystem.controllers.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kstn.taskmanagementsystem.dtos.user.RequestUserDto;
import ru.kstn.taskmanagementsystem.dtos.user.ResponseUserDto;
import ru.kstn.taskmanagementsystem.services.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin user API", description = "Operations related to user management")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get list of users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> getUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getUserById(
            @Parameter(description = "ID of the user to be retrieved", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ResponseUserDto> createUser(
            @Parameter(description = "User object to be created", required = true)
            @Valid @RequestBody RequestUserDto request
            ) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDto> updateUser(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user object", required = true)
            @Valid @RequestBody RequestUserDto request
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Delete user", description = "Deletes an existing user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
