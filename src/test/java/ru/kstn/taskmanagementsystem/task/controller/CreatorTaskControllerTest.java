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
import ru.kstn.taskmanagementsystem.task.dto.RequestTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.task.enums.TaskStatus;
import ru.kstn.taskmanagementsystem.task.service.TaskService;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CreatorTaskController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class CreatorTaskControllerTest {

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
    void testGetTaskListByCreatorUsername() throws Exception {
        SimpleTaskDto dto = new SimpleTaskDto();
        List<SimpleTaskDto> taskList = List.of(dto);

        when(taskService.getTaskListByCreatorUsername(eq("creator1"), any(Pageable.class)))
                .thenReturn(taskList);

        mockMvc.perform(get("/api/v1/creator/tasks/creators")
                        .param("page", "0")
                        .param("size", "10")
                        .param("username", "creator1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(taskList.size()));

        verify(taskService).getTaskListByCreatorUsername(eq("creator1"), any(Pageable.class));
    }

    @Test
    void testCreateTask() throws Exception {
        RequestTaskDto request = new RequestTaskDto();
        request.setTitle("title");
        request.setDescription("description");
        request.setCreatorId(1L);
        request.setPerformerId(2L);
        request.setStatus(TaskStatus.AWAITING);
        request.setPriority(TaskPriority.HIGH);

        ResponseTaskDto responseDto = new ResponseTaskDto();
        when(taskService.createTask(any(RequestTaskDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/creator/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(taskService).createTask(any(RequestTaskDto.class));
    }

    @Test
    void testUpdateTask() throws Exception {
        Long taskId = 1L;
        RequestTaskDto request = new RequestTaskDto();
        request.setTitle("title");
        request.setDescription("description");
        request.setCreatorId(1L);
        request.setPerformerId(2L);
        request.setStatus(TaskStatus.AWAITING);
        request.setPriority(TaskPriority.HIGH);

        ResponseTaskDto responseDto = new ResponseTaskDto();
        when(taskService.updateTask(eq(taskId), any(RequestTaskDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/creator/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(taskService).updateTask(eq(taskId), any(RequestTaskDto.class));
    }

    @Test
    void testUpdatePerformersList() throws Exception {
        Long taskId = 1L;
        Long performerId = 100L;
        ResponseTaskDto responseDto = new ResponseTaskDto();
        when(taskService.updatePerformerToList(taskId, performerId)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/creator/tasks/{id}/performers", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(performerId)))
                .andExpect(status().isOk());

        verify(taskService).updatePerformerToList(taskId, performerId);
    }

    @Test
    void testChangePriority() throws Exception {
        Long taskId = 1L;
        TaskPriority priority = TaskPriority.HIGH;
        ResponseTaskDto responseDto = new ResponseTaskDto();
        when(taskService.changePriority(taskId, priority)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/creator/tasks/{id}/priority", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priority)))
                .andExpect(status().isOk());

        verify(taskService).changePriority(taskId, priority);
    }
}
