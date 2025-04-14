package ru.kstn.taskmanagementsystem.controllers.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.kstn.taskmanagementsystem.dtos.auth.JwtCookieResponse;
import ru.kstn.taskmanagementsystem.dtos.auth.LoginRequest;
import ru.kstn.taskmanagementsystem.dtos.auth.RegisterRequest;
import ru.kstn.taskmanagementsystem.services.auth.AuthService;
import ru.kstn.taskmanagementsystem.services.auth.CookieService;
import ru.kstn.taskmanagementsystem.services.auth.JwtService;
import ru.kstn.taskmanagementsystem.controllers.auth.AuthController;
import ru.kstn.taskmanagementsystem.enums.user.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @MockitoBean
    private CookieService cookieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegistration() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setEmail("register@example.com");
        request.setPassword("password");
        request.setRole(UserRole.CREATOR);

        ResponseCookie accessCookie = ResponseCookie.from("accessTokenCookie", "accessValue").build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshTokenCookie", "refreshValue").build();
        JwtCookieResponse jwtResponse = new JwtCookieResponse(accessCookie, refreshCookie);
        when(authService.register(any(RegisterRequest.class))).thenReturn(jwtResponse);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        List<String> setCookieHeaders = mvcResult.getResponse().getHeaders(HttpHeaders.SET_COOKIE);

        assertTrue(setCookieHeaders.contains(accessCookie.toString()));
        assertTrue(setCookieHeaders.contains(refreshCookie.toString()));
        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("login@example.com");
        request.setPassword("password");

        ResponseCookie accessCookie = ResponseCookie.from("accessTokenCookie", "accessValue").build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshTokenCookie", "refreshValue").build();
        JwtCookieResponse jwtResponse = new JwtCookieResponse(accessCookie, refreshCookie);
        when(authService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        List<String> setCookieHeaders = mvcResult.getResponse().getHeaders(HttpHeaders.SET_COOKIE);

        assertTrue(setCookieHeaders.contains(accessCookie.toString()));
        assertTrue(setCookieHeaders.contains(refreshCookie.toString()));
        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void testRefresh() throws Exception {
        ResponseCookie accessCookie = ResponseCookie.from("accessTokenCookie", "accessValue").build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshTokenCookie", "refreshValue").build();
        JwtCookieResponse jwtResponse = new JwtCookieResponse(accessCookie, refreshCookie);
        when(authService.refreshToken(any(HttpServletRequest.class))).thenReturn(jwtResponse);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<String> setCookieHeaders = mvcResult.getResponse().getHeaders(HttpHeaders.SET_COOKIE);

        assertTrue(setCookieHeaders.contains(accessCookie.toString()));
        assertTrue(setCookieHeaders.contains(refreshCookie.toString()));
        verify(authService).refreshToken(any(HttpServletRequest.class));
    }
}
