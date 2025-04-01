package ru.kstn.taskmanagementsystem.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.comment.dto.RequestCommentDto;
import ru.kstn.taskmanagementsystem.comment.dto.ResponseCommentDto;
import ru.kstn.taskmanagementsystem.comment.entity.Comment;
import ru.kstn.taskmanagementsystem.task.mapper.SimpleTaskMapper;
import ru.kstn.taskmanagementsystem.user.mapper.SimpleUserMapper;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SimpleUserMapper.class, SimpleTaskMapper.class})
public interface CommentMapper {
    ResponseCommentDto toResponseCommentDto(Comment comment);
    Comment toComment(RequestCommentDto request);
}
