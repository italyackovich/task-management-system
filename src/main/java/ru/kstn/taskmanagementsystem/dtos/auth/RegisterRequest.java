package ru.kstn.taskmanagementsystem.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.kstn.taskmanagementsystem.model.enums.UserRole;

@Data
@Schema(description = "Request DTO for user registration")
public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank")
    @Schema(description = "The username of the user", example = "john_doe")
    private String username;

    @Email(message = "Email is incorrect")
    @Schema(description = "The email address of the user", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "The password of the user", example = "Pa$$w0rd")
    private String password;

    @NotNull(message = "Role cannot be a null")
    @Schema(description = "The role of the user", example = "CREATOR")
    private UserRole role;
}
