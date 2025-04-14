package ru.kstn.taskmanagementsystem.mappers.task;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.dtos.task.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.entities.task.Task;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SimpleTaskMapper {
    SimpleTaskDto toSimpleTaskDto(Task task);
}
