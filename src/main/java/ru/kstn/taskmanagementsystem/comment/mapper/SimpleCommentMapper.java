package ru.kstn.taskmanagementsystem.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.comment.dto.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.comment.entity.Comment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SimpleCommentMapper {
    SimpleCommentDto toSimpleCommentDto(Comment comment);
}
