package ru.kstn.taskmanagementsystem.mappers.comment;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.dtos.comment.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.entities.comment.Comment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SimpleCommentMapper {
    SimpleCommentDto toSimpleCommentDto(Comment comment);
}
