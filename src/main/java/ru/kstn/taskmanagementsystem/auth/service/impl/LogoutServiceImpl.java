package ru.kstn.taskmanagementsystem.auth.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.auth.service.CookieService;
import ru.kstn.taskmanagementsystem.auth.service.JwtService;
import ru.kstn.taskmanagementsystem.auth.service.LogoutService;
import ru.kstn.taskmanagementsystem.auth.service.TokenService;
import ru.kstn.taskmanagementsystem.user.entity.User;
import ru.kstn.taskmanagementsystem.user.exception.UserNotFoundException;
import ru.kstn.taskmanagementsystem.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final CookieService cookieService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String token;

        token = cookieService.getValueFromCookie(request, "accessTokenCookie");
        String userEmail = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));

        tokenService.revokeAllUserTokens(user);

        cookieService.deleteCookie("accessTokenCookie");
        cookieService.deleteCookie("refreshTokenCookie");
        SecurityContextHolder.clearContext();
    }
}
