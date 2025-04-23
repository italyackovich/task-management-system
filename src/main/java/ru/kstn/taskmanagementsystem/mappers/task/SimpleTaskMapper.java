package ru.kstn.taskmanagementsystem.mappers.task;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.kstn.taskmanagementsystem.dtos.task.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.model.entity.Task;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SimpleTaskMapper {
    SimpleTaskDto toSimpleTaskDto(Task task);
}
