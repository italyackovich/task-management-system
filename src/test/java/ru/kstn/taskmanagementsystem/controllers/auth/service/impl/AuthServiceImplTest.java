package ru.kstn.taskmanagementsystem.controllers.auth.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.kstn.taskmanagementsystem.dtos.auth.JwtCookieResponse;
import ru.kstn.taskmanagementsystem.dtos.auth.LoginRequest;
import ru.kstn.taskmanagementsystem.dtos.auth.RegisterRequest;
import ru.kstn.taskmanagementsystem.model.entity.Token;
import ru.kstn.taskmanagementsystem.mappers.auth.AuthMapper;
import ru.kstn.taskmanagementsystem.repositories.auth.TokenRepository;
import ru.kstn.taskmanagementsystem.model.CustomUserDetails;
import ru.kstn.taskmanagementsystem.services.auth.CookieService;
import ru.kstn.taskmanagementsystem.services.auth.JwtService;
import ru.kstn.taskmanagementsystem.services.auth.TokenService;
import ru.kstn.taskmanagementsystem.services.auth.impl.AuthServiceImpl;
import ru.kstn.taskmanagementsystem.model.entity.User;
import ru.kstn.taskmanagementsystem.repositories.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private JwtService jwtService;
    @Mock private AuthMapper authMapper;
    @Mock private TokenService tokenService;
    @Mock private CookieService cookieService;
    @Mock private UserRepository userRepository;
    @Mock private TokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authManager;
    @Mock private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "accessExpiration", 3600000L);
    }


    @Test
    void testRegister() {
        Long accessExpiration = 3600000L;

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("plainPassword");

        User user = new User();
        user.setEmail(request.getEmail());

        when(authMapper.toUser(request)).thenReturn(user);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        when(jwtService.generateAccessToken(any(CustomUserDetails.class))).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(any(CustomUserDetails.class))).thenReturn(refreshToken);

        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOps);

        JwtCookieResponse expectedResponse = new JwtCookieResponse(
                ResponseCookie.from("accessTokenCookie", accessToken).build(),
                ResponseCookie.from("refreshTokenCookie", refreshToken).build()
        );
        when(jwtService.buildJwtCookieResponse(accessToken, refreshToken)).thenReturn(expectedResponse);

        JwtCookieResponse result = authService.register(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(stringRedisTemplate.opsForValue())
                .set(eq(user.getEmail()), eq(accessToken), eq(accessExpiration), any(TimeUnit.class));
    }


    @Test
    void testLogin() {
        Long accessExpiration = 3600000L;

        LoginRequest request = new LoginRequest();
        request.setEmail("login@example.com");
        request.setPassword("password123");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = new User();
        user.setEmail(request.getEmail());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        when(jwtService.generateAccessToken(any(CustomUserDetails.class))).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(any(CustomUserDetails.class))).thenReturn(refreshToken);

        doNothing().when(tokenService).revokeAllUserTokens(user);

        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOps);

        ResponseCookie accessCookie = ResponseCookie.from("accessTokenCookie", accessToken).build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshTokenCookie", refreshToken).build();
        JwtCookieResponse expectedResponse = new JwtCookieResponse(accessCookie, refreshCookie);
        when(jwtService.buildJwtCookieResponse(accessToken, refreshToken)).thenReturn(expectedResponse);

        JwtCookieResponse result = authService.login(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).revokeAllUserTokens(user);
        verify(stringRedisTemplate.opsForValue())
                .set(eq(user.getEmail()), eq(accessToken), eq(accessExpiration), any(TimeUnit.class));
    }

    @Test
    void testRefreshToken() {
        Long accessExpiration = 3600000L;
        ReflectionTestUtils.setField(authService, "accessExpiration", accessExpiration);

        HttpServletRequest request = mock(HttpServletRequest.class);
        String refreshToken = "refreshToken";
        when(cookieService.getValueFromCookie(request, "refreshTokenCookie")).thenReturn(refreshToken);

        Token dummyToken = new Token();
        dummyToken.setIsRevoked(false);
        when(tokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(dummyToken));

        String email = "refresh@example.com";
        when(jwtService.extractUsername(refreshToken)).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        when(jwtService.isTokenValid(eq(refreshToken), any(CustomUserDetails.class))).thenReturn(true);

        String newAccessToken = "newAccessToken";
        when(jwtService.generateAccessToken(any(CustomUserDetails.class))).thenReturn(newAccessToken);

        doNothing().when(tokenService).revokeAllUserTokens(user);

        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOps);

        ResponseCookie accessCookie = ResponseCookie.from("accessTokenCookie", newAccessToken).build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshTokenCookie", refreshToken).build();
        JwtCookieResponse expectedResponse = new JwtCookieResponse(accessCookie, refreshCookie);
        when(jwtService.buildJwtCookieResponse(newAccessToken, refreshToken)).thenReturn(expectedResponse);

        JwtCookieResponse result = authService.refreshToken(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(stringRedisTemplate.opsForValue())
                .set(eq(user.getEmail()), eq(newAccessToken), eq(accessExpiration), any(TimeUnit.class));
        verify(tokenService).revokeAllUserTokens(user);
        verify(cookieService).getValueFromCookie(request, "refreshTokenCookie");
    }
}