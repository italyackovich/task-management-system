package ru.kstn.taskmanagementsystem.mappers.comment;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.dtos.comment.RequestCommentDto;
import ru.kstn.taskmanagementsystem.dtos.comment.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.entities.comment.Comment;
import ru.kstn.taskmanagementsystem.mappers.task.SimpleTaskMapper;
import ru.kstn.taskmanagementsystem.mappers.user.SimpleUserMapper;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SimpleUserMapper.class, SimpleTaskMapper.class})
public interface CommentMapper {
    ResponseCommentDto toResponseCommentDto(Comment comment);
    Comment toComment(RequestCommentDto request);
}
