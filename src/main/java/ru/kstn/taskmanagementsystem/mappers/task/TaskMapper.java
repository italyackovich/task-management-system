package ru.kstn.taskmanagementsystem.mappers.task;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.mappers.comment.SimpleCommentMapper;
import ru.kstn.taskmanagementsystem.dtos.task.RequestTaskDto;
import ru.kstn.taskmanagementsystem.dtos.task.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.model.entity.Task;
import ru.kstn.taskmanagementsystem.mappers.user.SimpleUserMapper;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SimpleUserMapper.class, SimpleCommentMapper.class})
public interface TaskMapper {
    Task toTask(RequestTaskDto taskCreateRequest);
    ResponseTaskDto toResponseTaskDto(Task task);
}
