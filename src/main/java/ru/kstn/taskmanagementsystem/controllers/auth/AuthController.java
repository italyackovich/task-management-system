package ru.kstn.taskmanagementsystem.controllers.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kstn.taskmanagementsystem.dtos.auth.JwtCookieResponse;
import ru.kstn.taskmanagementsystem.dtos.auth.LoginRequest;
import ru.kstn.taskmanagementsystem.dtos.auth.RegisterRequest;
import ru.kstn.taskmanagementsystem.services.auth.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "Operations related to user registration, login, and token refresh")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User Registration", description = "Registers a new user and returns JWT tokens in cookies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid registration details")
    })
    @PostMapping("/accounts")
    public ResponseEntity<?> register(
            @Parameter(description = "Registration request payload", required = true)
            @Valid @RequestBody RegisterRequest request) {
        JwtCookieResponse jwtCookieResponse = authService.register(request);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookieResponse.getAccessToken().toString())
                .header(HttpHeaders.SET_COOKIE, jwtCookieResponse.getRefreshToken().toString())
                .build();
    }

    @Operation(summary = "User Login", description = "Authenticates a user and returns JWT tokens in cookies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/sessions")
    public ResponseEntity<?> login(
            @Parameter(description = "Login request payload", required = true)
            @Valid @RequestBody LoginRequest request) {
        JwtCookieResponse jwtCookieResponse = authService.login(request);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookieResponse.getAccessToken().toString())
                .header(HttpHeaders.SET_COOKIE, jwtCookieResponse.getRefreshToken().toString())
                .build();
    }

    @Operation(summary = "Refresh Token", description = "Refreshes JWT tokens using the provided refresh token cookie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens successfully refreshed"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/tokens")
    public ResponseEntity<?> refresh(
            @Parameter(description = "HTTP request containing the refresh token cookie", required = true)
            HttpServletRequest request) {
        JwtCookieResponse jwtCookieResponse = authService.refreshToken(request);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookieResponse.getAccessToken().toString())
                .header(HttpHeaders.SET_COOKIE, jwtCookieResponse.getRefreshToken().toString())
                .build();
    }
}
