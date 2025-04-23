package ru.kstn.taskmanagementsystem.dtos.task;

import lombok.Data;
import ru.kstn.taskmanagementsystem.model.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.model.enums.TaskStatus;

@Data
public class SimpleTaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
}
