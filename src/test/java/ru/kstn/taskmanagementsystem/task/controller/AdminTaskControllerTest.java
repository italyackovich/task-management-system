package ru.kstn.taskmanagementsystem.task.controller;

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
import ru.kstn.taskmanagementsystem.controllers.task.AdminTaskController;
import ru.kstn.taskmanagementsystem.dtos.task.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.services.task.TaskService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AdminTaskController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AdminTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

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
    void testGetTaskById_Success() throws Exception {
        Long taskId = 1L;
        ResponseTaskDto dto = new ResponseTaskDto();
        when(taskService.getTaskById(taskId)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/admin/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(taskService).getTaskById(taskId);
    }

    @Test
    void testDeleteTask_Success() throws Exception {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTaskById(taskId);

        mockMvc.perform(delete("/api/v1/admin/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTaskById(taskId);
    }
}
