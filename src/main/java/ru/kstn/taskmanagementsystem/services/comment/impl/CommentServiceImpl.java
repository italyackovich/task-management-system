package ru.kstn.taskmanagementsystem.services.comment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.dtos.comment.RequestCommentDto;
import ru.kstn.taskmanagementsystem.dtos.comment.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.dtos.comment.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.entities.comment.Comment;
import ru.kstn.taskmanagementsystem.exceptions.comment.CommentCreatorNotPerformerException;
import ru.kstn.taskmanagementsystem.exceptions.comment.CommentNotFoundException;
import ru.kstn.taskmanagementsystem.mappers.comment.CommentMapper;
import ru.kstn.taskmanagementsystem.mappers.comment.SimpleCommentMapper;
import ru.kstn.taskmanagementsystem.repositories.comment.CommentRepository;
import ru.kstn.taskmanagementsystem.services.comment.CommentService;
import ru.kstn.taskmanagementsystem.entities.task.Task;
import ru.kstn.taskmanagementsystem.exceptions.task.TaskNotFoundException;
import ru.kstn.taskmanagementsystem.repositories.task.TaskRepository;
import ru.kstn.taskmanagementsystem.entities.user.User;
import ru.kstn.taskmanagementsystem.enums.user.UserRole;
import ru.kstn.taskmanagementsystem.exceptions.user.UserNotFoundException;
import ru.kstn.taskmanagementsystem.repositories.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final SimpleCommentMapper simpleCommentMapper;

    @Override
    public ResponseCommentDto createComment(RequestCommentDto request) {
        Comment comment = commentMapper.toComment(request);
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + request.getTaskId() + " not found"));
        User user = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.getCreatorId() + " not found"));
        comment.setCreator(user);
        comment.setTask(task);
        if (user.getRole().equals(UserRole.ADMIN)) {
            return commentMapper.toResponseCommentDto(commentRepository.save(comment));
        }
        if (task.getPerformerList().stream()
                .noneMatch(performer -> performer.getUsername().equals(comment.getCreator().getUsername()))
        ) {
            throw new CommentCreatorNotPerformerException("User with id " + request.getCreatorId() + " is not a performer for this task");
        }
        return commentMapper.toResponseCommentDto(commentRepository.save(comment));
    }

    @Override
    public ResponseCommentDto updateComment(Long commentId, RequestCommentDto request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + request.getTaskId() + " not found"));
        User user = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.getCreatorId() + " not found"));
        return commentRepository.findById(commentId).map(comment -> {
            Comment newComment = new Comment();
            newComment.setId(comment.getId());
            newComment.setContent(request.getContent());
            newComment.setCreator(user);
            newComment.setTask(task);
            return commentMapper.toResponseCommentDto(commentRepository.save(newComment));
        }).orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<ResponseCommentDto> getCommentList() {
        return commentRepository.findAll().stream().map(commentMapper::toResponseCommentDto).toList();
    }

    @Override
    public ResponseCommentDto getCommentById(Long id) {
        return commentMapper.toResponseCommentDto(commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found")));
    }

    @Override
    public List<SimpleCommentDto> getCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream().map(simpleCommentMapper::toSimpleCommentDto).toList();
    }
}
