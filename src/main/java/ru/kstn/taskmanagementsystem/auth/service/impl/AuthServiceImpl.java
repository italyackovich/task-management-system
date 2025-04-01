package ru.kstn.taskmanagementsystem.auth.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kstn.taskmanagementsystem.auth.dto.JwtCookieResponse;
import ru.kstn.taskmanagementsystem.auth.dto.LoginRequest;
import ru.kstn.taskmanagementsystem.auth.dto.RegisterRequest;
import ru.kstn.taskmanagementsystem.auth.exception.InvalidTokenException;
import ru.kstn.taskmanagementsystem.auth.exception.RevokedTokenException;
import ru.kstn.taskmanagementsystem.auth.exception.TokenNotFoundException;
import ru.kstn.taskmanagementsystem.auth.mapper.AuthMapper;
import ru.kstn.taskmanagementsystem.auth.repository.TokenRepository;
import ru.kstn.taskmanagementsystem.auth.security.CustomUserDetails;
import ru.kstn.taskmanagementsystem.auth.service.AuthService;
import ru.kstn.taskmanagementsystem.auth.service.CookieService;
import ru.kstn.taskmanagementsystem.auth.service.JwtService;
import ru.kstn.taskmanagementsystem.auth.service.TokenService;
import ru.kstn.taskmanagementsystem.user.entity.User;
import ru.kstn.taskmanagementsystem.user.exception.UserNotFoundException;
import ru.kstn.taskmanagementsystem.user.repository.UserRepository;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.accessToken.expiration}")
    private Long accessExpiration;

    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final TokenService tokenService;
    private final CookieService cookieService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional
    @Override
    public JwtCookieResponse register(RegisterRequest request) {
        User user = authMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateAccessToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);
        stringRedisTemplate.opsForValue().set(user.getEmail(), accessToken, accessExpiration, TimeUnit.MILLISECONDS);
        return jwtService.buildJwtCookieResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public JwtCookieResponse login(LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + request.getEmail() + " not found"));
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateAccessToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);
        tokenService.revokeAllUserTokens(user);
        stringRedisTemplate.opsForValue().set(user.getEmail(), accessToken, accessExpiration, TimeUnit.MILLISECONDS);
        return jwtService.buildJwtCookieResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public JwtCookieResponse refreshToken(HttpServletRequest request) {
        String refreshToken = cookieService.getValueFromCookie(request, "refreshTokenCookie");
        boolean isRefreshTokenValid = tokenRepository.findByToken(refreshToken)
                .map(token -> !token.getIsRevoked())
                .orElseThrow(() -> new TokenNotFoundException("Token by refresh token " + refreshToken + " not found" ));
        if (!isRefreshTokenValid) {
            throw new RevokedTokenException("Token " + refreshToken + " is revoked");
        }

        String email = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        if (!jwtService.isTokenValid(refreshToken, customUserDetails)) {
            throw new InvalidTokenException("Token by user with id " + user.getId() + " is invalid");
        }

        String accessToken = jwtService.generateAccessToken(customUserDetails);
        tokenService.revokeAllUserTokens(user);

        stringRedisTemplate.opsForValue().set(user.getEmail(), accessToken, accessExpiration, TimeUnit.MILLISECONDS);

        return jwtService.buildJwtCookieResponse(accessToken, refreshToken);
    }
}
