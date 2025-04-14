package ru.kstn.taskmanagementsystem.services.auth;

import jakarta.servlet.http.HttpServletRequest;
import ru.kstn.taskmanagementsystem.dtos.auth.JwtCookieResponse;
import ru.kstn.taskmanagementsystem.dtos.auth.LoginRequest;
import ru.kstn.taskmanagementsystem.dtos.auth.RegisterRequest;

public interface AuthService {
    JwtCookieResponse register(RegisterRequest registerRequest);
    JwtCookieResponse login(LoginRequest loginRequest);
    JwtCookieResponse refreshToken(HttpServletRequest httpServletRequest);
}
