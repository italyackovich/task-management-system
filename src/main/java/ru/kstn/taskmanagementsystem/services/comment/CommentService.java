package ru.kstn.taskmanagementsystem.services.comment;

import ru.kstn.taskmanagementsystem.dtos.comment.RequestCommentDto;
import ru.kstn.taskmanagementsystem.dtos.comment.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.dtos.comment.SimpleCommentDto;

import java.util.List;

public interface CommentService {
    List<ResponseCommentDto> getCommentList();
    ResponseCommentDto getCommentById(Long id);
    List<SimpleCommentDto> getCommentsByTaskId(Long id);
    ResponseCommentDto createComment(RequestCommentDto request);
    ResponseCommentDto updateComment(Long commentId, RequestCommentDto request);
    void deleteComment(Long commentId);
}
