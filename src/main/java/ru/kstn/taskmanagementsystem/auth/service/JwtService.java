package ru.kstn.taskmanagementsystem.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kstn.taskmanagementsystem.auth.dto.JwtCookieResponse;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateAccessToken(UserDetails userDetails);
    String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    JwtCookieResponse buildJwtCookieResponse(String accessToken, String refreshToken);
    boolean isTokenValid(String token, UserDetails userDetails);
}
