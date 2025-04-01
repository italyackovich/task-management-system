package ru.kstn.taskmanagementsystem.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.comment.mapper.SimpleCommentMapper;
import ru.kstn.taskmanagementsystem.task.dto.RequestTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.task.entity.Task;
import ru.kstn.taskmanagementsystem.user.mapper.SimpleUserMapper;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SimpleUserMapper.class, SimpleCommentMapper.class})
public interface TaskMapper {
    Task toTask(RequestTaskDto taskCreateRequest);
    ResponseTaskDto toResponseTaskDto(Task task);
}
