package ru.kstn.taskmanagementsystem.comment.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kstn.taskmanagementsystem.comment.dto.RequestCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.comment.entity.Comment;
import ru.kstn.taskmanagementsystem.comment.exception.CommentCreatorNotPerformerException;
import ru.kstn.taskmanagementsystem.comment.exception.CommentNotFoundException;
import ru.kstn.taskmanagementsystem.comment.mapper.CommentMapper;
import ru.kstn.taskmanagementsystem.comment.mapper.SimpleCommentMapper;
import ru.kstn.taskmanagementsystem.comment.repository.CommentRepository;
import ru.kstn.taskmanagementsystem.task.entity.Task;
import ru.kstn.taskmanagementsystem.task.repository.TaskRepository;
import ru.kstn.taskmanagementsystem.user.entity.User;
import ru.kstn.taskmanagementsystem.user.enums.UserRole;
import ru.kstn.taskmanagementsystem.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private SimpleCommentMapper simpleCommentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void testCreateComment_AdminUser() {
        RequestCommentDto request = new RequestCommentDto();
        request.setTaskId(1L);
        request.setCreatorId(10L);
        request.setContent("Test comment");

        Comment comment = new Comment();
        Task task = new Task();
        User adminUser = new User();
        adminUser.setRole(UserRole.ADMIN);

        when(commentMapper.toComment(request)).thenReturn(comment);
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));
        when(userRepository.findById(request.getCreatorId())).thenReturn(Optional.of(adminUser));
        when(commentRepository.save(comment)).thenReturn(comment);
        ResponseCommentDto responseDto = new ResponseCommentDto();
        when(commentMapper.toResponseCommentDto(comment)).thenReturn(responseDto);

        ResponseCommentDto result = commentService.createComment(request);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(commentRepository).save(comment);
    }

    @Test
    void testCreateComment_CreatorUser() {
        RequestCommentDto request = new RequestCommentDto();
        request.setTaskId(1L);
        request.setCreatorId(20L);
        request.setContent("Test comment");

        Comment comment = new Comment();
        Task task = new Task();
        task.setPerformerList(Collections.emptyList());
        User creator = new User();
        creator.setRole(UserRole.CREATOR);

        when(commentMapper.toComment(request)).thenReturn(comment);
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));
        when(userRepository.findById(request.getCreatorId())).thenReturn(Optional.of(creator));

        CommentCreatorNotPerformerException ex = assertThrows(CommentCreatorNotPerformerException.class,
                () -> commentService.createComment(request));
        assertTrue(ex.getMessage().contains("User with id " + request.getCreatorId() + " is not a performer for this task"));
    }

    @Test
    void testCreateComment_PerformerUser() {
        RequestCommentDto request = new RequestCommentDto();
        request.setTaskId(1L);
        request.setCreatorId(30L);
        request.setContent("Test comment");

        Comment comment = new Comment();
        Task task = new Task();
        User performer = new User();
        performer.setUsername("user30");
        performer.setRole(UserRole.PERFORMER);
        task.setPerformerList(List.of(performer));

        when(commentMapper.toComment(request)).thenReturn(comment);
        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));
        when(userRepository.findById(request.getCreatorId())).thenReturn(Optional.of(performer));
        when(commentRepository.save(comment)).thenReturn(comment);
        ResponseCommentDto responseDto = new ResponseCommentDto();
        when(commentMapper.toResponseCommentDto(comment)).thenReturn(responseDto);

        ResponseCommentDto result = commentService.createComment(request);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(commentRepository).save(comment);
    }

    @Test
    void testUpdateComment_CommentExists() {
        Long commentId = 1L;
        RequestCommentDto request = new RequestCommentDto();
        request.setTaskId(1L);
        request.setCreatorId(10L);
        request.setContent("Updated comment");

        Task task = new Task();
        User user = new User();
        Comment existingComment = new Comment();
        existingComment.setId(commentId);

        Comment newComment = new Comment();
        newComment.setId(commentId);
        newComment.setContent(request.getContent());
        newComment.setCreator(user);
        newComment.setTask(task);

        ResponseCommentDto responseDto = new ResponseCommentDto();

        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(task));
        when(userRepository.findById(request.getCreatorId())).thenReturn(Optional.of(user));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(newComment);
        when(commentMapper.toResponseCommentDto(newComment)).thenReturn(responseDto);

        ResponseCommentDto result = commentService.updateComment(commentId, request);

        assertNotNull(result);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(newComment);
        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateComment_CommentNotFound() {
        Long commentId = 1L;
        RequestCommentDto request = new RequestCommentDto();
        request.setTaskId(1L);
        request.setCreatorId(10L);
        request.setContent("Updated comment");

        when(taskRepository.findById(request.getTaskId())).thenReturn(Optional.of(new Task()));
        when(userRepository.findById(request.getCreatorId())).thenReturn(Optional.of(new User()));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        CommentNotFoundException ex = assertThrows(CommentNotFoundException.class, () ->
                commentService.updateComment(commentId, request));
        assertTrue(ex.getMessage().contains("Comment with id " + commentId + " not found"));
    }

    @Test
    void testDeleteComment() {
        Long commentId = 1L;
        doNothing().when(commentRepository).deleteById(commentId);

        commentService.deleteComment(commentId);

        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void testGetCommentList() {
        Comment comment = new Comment();
        ResponseCommentDto dto = new ResponseCommentDto();
        List<Comment> comments = List.of(comment);
        when(commentRepository.findAll()).thenReturn(comments);
        when(commentMapper.toResponseCommentDto(comment)).thenReturn(dto);

        List<ResponseCommentDto> result = commentService.getCommentList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void testGetCommentById_Exists() {
        Long commentId = 1L;
        Comment comment = new Comment();
        ResponseCommentDto dto = new ResponseCommentDto();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentMapper.toResponseCommentDto(comment)).thenReturn(dto);

        ResponseCommentDto result = commentService.getCommentById(commentId);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void testGetCommentById_NotFound() {
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        CommentNotFoundException ex = assertThrows(CommentNotFoundException.class, () ->
                commentService.getCommentById(commentId));
        assertTrue(ex.getMessage().contains("Comment with id " + commentId + " not found"));
    }

    @Test
    void testGetCommentsByTaskId() {
        Long taskId = 1L;
        Comment comment = new Comment();
        SimpleCommentDto simpleDto = new SimpleCommentDto();
        List<Comment> comments = List.of(comment);
        when(commentRepository.findAllByTaskId(taskId)).thenReturn(comments);
        when(simpleCommentMapper.toSimpleCommentDto(comment)).thenReturn(simpleDto);

        List<SimpleCommentDto> result = commentService.getCommentsByTaskId(taskId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(simpleDto, result.getFirst());
    }
}

