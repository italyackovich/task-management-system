package ru.kstn.taskmanagementsystem.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kstn.taskmanagementsystem.services.auth.CookieService;
import ru.kstn.taskmanagementsystem.services.auth.JwtService;
import ru.kstn.taskmanagementsystem.controllers.user.UserController;
import ru.kstn.taskmanagementsystem.dtos.user.RequestUserDto;
import ru.kstn.taskmanagementsystem.dtos.user.ResponseUserDto;
import ru.kstn.taskmanagementsystem.enums.user.UserRole;
import ru.kstn.taskmanagementsystem.services.user.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

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
    void testGetUserList() throws Exception {
        ResponseUserDto dto1 = new ResponseUserDto();
        ResponseUserDto dto2 = new ResponseUserDto();
        List<ResponseUserDto> userList = List.of(dto1, dto2);
        when(userService.getUserList()).thenReturn(userList);

        mockMvc.perform(get("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(userList.size()));

        verify(userService).getUserList();
    }

    @Test
    void testGetUserById() throws Exception {
        Long userId = 1L;
        ResponseUserDto dto = new ResponseUserDto();
        when(userService.getUserById(userId)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getUserById(userId);
    }

    @Test
    void testCreateUser() throws Exception {
        // Arrange
        RequestUserDto request = new RequestUserDto();
        request.setUsername("john_doe");
        request.setEmail("john@example.com");
        request.setPassword("secret");
        request.setRole(UserRole.CREATOR);

        ResponseUserDto dto = new ResponseUserDto();
        when(userService.createUser(any(RequestUserDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).createUser(any(RequestUserDto.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        RequestUserDto request = new RequestUserDto();
        request.setUsername("updated_user");
        request.setEmail("updated@example.com");
        request.setPassword("newSecret");
        request.setRole(UserRole.ADMIN);

        ResponseUserDto dto = new ResponseUserDto();
        when(userService.updateUser(eq(userId), any(RequestUserDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).updateUser(eq(userId), any(RequestUserDto.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }
}
