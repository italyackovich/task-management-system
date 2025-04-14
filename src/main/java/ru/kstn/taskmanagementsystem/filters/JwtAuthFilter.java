package ru.kstn.taskmanagementsystem.filters;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kstn.taskmanagementsystem.services.auth.CookieService;
import ru.kstn.taskmanagementsystem.services.auth.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CookieService cookieService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        if (shouldSkipFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = extractJwt(request, response);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            setAuthenticationIfValid(accessToken, request);
        } catch (ExpiredJwtException exception) {
            handleExceptions(response, "{\"Expired jwt\": " + "\"" + exception.getMessage() + "\"" + "}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getServletPath().toLowerCase().trim();
        return path.contains("/api/v1/auth") ||
                path.contains("/v3/api-docs") ||
                path.contains("/swagger-ui");
    }

    private String extractJwt(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            return cookieService.getValueFromCookie(request, "accessTokenCookie");
        } catch (Exception exception) {
            handleExceptions(response, "{\"Missing cookies\": " + "\"" + exception.getMessage() + "\"" + "}");
            return null;
        }
    }

    private void setAuthenticationIfValid(String jwt, HttpServletRequest request) {
        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            boolean isAccessTokenValid = Boolean.TRUE.equals(stringRedisTemplate.hasKey(userEmail));
            if (jwtService.isTokenValid(jwt, userDetails) && isAccessTokenValid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }

    private void handleExceptions(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
