package ru.kstn.taskmanagementsystem.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.comment.mapper.SimpleCommentMapper;
import ru.kstn.taskmanagementsystem.task.mapper.SimpleTaskMapper;
import ru.kstn.taskmanagementsystem.user.dto.RequestUserDto;
import ru.kstn.taskmanagementsystem.user.dto.ResponseUserDto;
import ru.kstn.taskmanagementsystem.user.entity.User;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SimpleTaskMapper.class, SimpleCommentMapper.class})
public interface UserMapper {
    ResponseUserDto toUserDto(User user);
    User toUser(RequestUserDto requestUserDto);
}
