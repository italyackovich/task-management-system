package ru.kstn.taskmanagementsystem.task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.entity.Task;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SimpleTaskMapper {
    SimpleTaskDto toSimpleTaskDto(Task task);
}
