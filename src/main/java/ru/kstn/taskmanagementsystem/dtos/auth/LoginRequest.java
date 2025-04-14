package ru.kstn.taskmanagementsystem.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request DTO for user login")
public class LoginRequest {

    @Email(message = "Email is incorrect")
    @Schema(description = "The email for the user", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Schema(description = "The email for the user", example = "Pa$$w0rd")
    private String password;
}
