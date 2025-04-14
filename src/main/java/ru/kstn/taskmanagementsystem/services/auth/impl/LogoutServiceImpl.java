package ru.kstn.taskmanagementsystem.services.auth.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.services.auth.CookieService;
import ru.kstn.taskmanagementsystem.services.auth.JwtService;
import ru.kstn.taskmanagementsystem.services.auth.LogoutService;
import ru.kstn.taskmanagementsystem.services.auth.TokenService;
import ru.kstn.taskmanagementsystem.entities.user.User;
import ru.kstn.taskmanagementsystem.exceptions.user.UserNotFoundException;
import ru.kstn.taskmanagementsystem.repositories.user.UserRepository;

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
