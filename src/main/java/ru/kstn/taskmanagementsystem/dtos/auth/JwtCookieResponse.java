package ru.kstn.taskmanagementsystem.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@AllArgsConstructor
public class JwtCookieResponse {
    private ResponseCookie accessToken;
    private ResponseCookie refreshToken;
}
