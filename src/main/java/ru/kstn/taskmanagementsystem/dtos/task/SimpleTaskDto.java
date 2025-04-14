package ru.kstn.taskmanagementsystem.dtos.task;

import lombok.Data;
import ru.kstn.taskmanagementsystem.enums.task.TaskPriority;
import ru.kstn.taskmanagementsystem.enums.task.TaskStatus;

@Data
public class SimpleTaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
}
