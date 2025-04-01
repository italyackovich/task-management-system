package ru.kstn.taskmanagementsystem.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.kstn.taskmanagementsystem.auth.dto.JwtCookieResponse;
import ru.kstn.taskmanagementsystem.auth.dto.LoginRequest;
import ru.kstn.taskmanagementsystem.auth.dto.RegisterRequest;

public interface AuthService {
    JwtCookieResponse register(RegisterRequest registerRequest);
    JwtCookieResponse login(LoginRequest loginRequest);
    JwtCookieResponse refreshToken(HttpServletRequest httpServletRequest);
}
