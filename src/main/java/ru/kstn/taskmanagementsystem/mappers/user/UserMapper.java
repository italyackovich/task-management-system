package ru.kstn.taskmanagementsystem.mappers.user;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.mappers.comment.SimpleCommentMapper;
import ru.kstn.taskmanagementsystem.mappers.task.SimpleTaskMapper;
import ru.kstn.taskmanagementsystem.dtos.user.RequestUserDto;
import ru.kstn.taskmanagementsystem.dtos.user.ResponseUserDto;
import ru.kstn.taskmanagementsystem.model.entity.User;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SimpleTaskMapper.class, SimpleCommentMapper.class})
public interface UserMapper {
    ResponseUserDto toUserDto(User user);
    User toUser(RequestUserDto requestUserDto);
}
