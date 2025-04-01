package ru.kstn.taskmanagementsystem.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.kstn.taskmanagementsystem.auth.service.CookieService;
import ru.kstn.taskmanagementsystem.auth.service.JwtService;
import ru.kstn.taskmanagementsystem.task.dto.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.enums.TaskStatus;
import ru.kstn.taskmanagementsystem.task.service.TaskService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PerformerTaskController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class PerformerTaskControllerTest {

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
    void testGetTaskListByPerformerUsername() throws Exception {
        SimpleTaskDto simpleDto = new SimpleTaskDto();
        List<SimpleTaskDto> taskList = List.of(simpleDto);

        when(taskService.getTaskListByPerformerUsername(eq("performer1"), any(Pageable.class)))
                .thenReturn(taskList);

        mockMvc.perform(get("/api/v1/performer/tasks/performers")
                        .param("page", "0")
                        .param("size", "10")
                        .param("username", "performer1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()));

        verify(taskService).getTaskListByPerformerUsername(eq("performer1"), any(Pageable.class));
    }

    @Test
    void testChangeStatus() throws Exception {
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.FINISHED;
        ResponseTaskDto responseDto = new ResponseTaskDto();

        when(taskService.changeStatus(taskId, newStatus)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/performer/tasks/{id}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(taskService).changeStatus(taskId, newStatus);
    }
}
