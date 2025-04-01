package ru.kstn.taskmanagementsystem.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@Data
@AllArgsConstructor
public class JwtCookieResponse {
    private ResponseCookie accessToken;
    private ResponseCookie refreshToken;
}
