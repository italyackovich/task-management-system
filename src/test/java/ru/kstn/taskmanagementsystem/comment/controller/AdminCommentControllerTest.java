package ru.kstn.taskmanagementsystem.comment.controller;

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
import ru.kstn.taskmanagementsystem.controllers.comment.AdminCommentController;
import ru.kstn.taskmanagementsystem.services.auth.CookieService;
import ru.kstn.taskmanagementsystem.services.auth.JwtService;
import ru.kstn.taskmanagementsystem.dtos.comment.RequestCommentDto;
import ru.kstn.taskmanagementsystem.dtos.comment.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.dtos.comment.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.services.comment.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AdminCommentController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AdminCommentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private CommentService commentService;

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
        void testGetCommentList() throws Exception {
                ResponseCommentDto dto1 = new ResponseCommentDto();
                ResponseCommentDto dto2 = new ResponseCommentDto();
                List<ResponseCommentDto> commentList = List.of(dto1, dto2);
                when(commentService.getCommentList()).thenReturn(commentList);

                mockMvc.perform(get("/api/v1/admin/comments")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(commentList.size()));

                verify(commentService).getCommentList();
        }

        @Test
        void testGetCommentsByTaskId() throws Exception {
                Long taskId = 1L;
                SimpleCommentDto simpleDto = new SimpleCommentDto();
                List<SimpleCommentDto> commentList = List.of(simpleDto);
                when(commentService.getCommentsByTaskId(taskId)).thenReturn(commentList);

                mockMvc.perform(get("/api/v1/admin/comments/{id}/task", taskId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(commentList.size()));

                verify(commentService).getCommentsByTaskId(taskId);
        }

        @Test
        void testGetCommentById() throws Exception {
                Long commentId = 1L;
                ResponseCommentDto dto = new ResponseCommentDto();
                when(commentService.getCommentById(commentId)).thenReturn(dto);

                mockMvc.perform(get("/api/v1/admin/comments/{id}", commentId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").exists());

                verify(commentService).getCommentById(commentId);
        }

        @Test
        void testUpdateComment() throws Exception {
                Long commentId = 1L;
                RequestCommentDto request = new RequestCommentDto();
                request.setContent("content");
                request.setCreatorId(1L);
                request.setTaskId(2L);

                ResponseCommentDto responseDto = new ResponseCommentDto();
                when(commentService.updateComment(eq(commentId), any(RequestCommentDto.class))).thenReturn(responseDto);

                mockMvc.perform(put("/api/v1/admin/comments/{id}", commentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

                verify(commentService).updateComment(eq(commentId), any(RequestCommentDto.class));
        }

        @Test
        void testDeleteComment() throws Exception {
                Long commentId = 1L;
                doNothing().when(commentService).deleteComment(commentId);

                mockMvc.perform(delete("/api/v1/admin/comments/{id}", commentId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

                verify(commentService).deleteComment(commentId);
        }
}
