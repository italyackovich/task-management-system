package ru.kstn.taskmanagementsystem.comment.service;

import ru.kstn.taskmanagementsystem.comment.dto.RequestCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.SimpleCommentDto;

import java.util.List;

public interface CommentService {
    List<ResponseCommentDto> getCommentList();
    ResponseCommentDto getCommentById(Long id);
    List<SimpleCommentDto> getCommentsByTaskId(Long id);
    ResponseCommentDto createComment(RequestCommentDto request);
    ResponseCommentDto updateComment(Long commentId, RequestCommentDto request);
    void deleteComment(Long commentId);
}
