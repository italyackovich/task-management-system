package ru.kstn.taskmanagementsystem.task.dto;

import lombok.Data;
import ru.kstn.taskmanagementsystem.comment.dto.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.task.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.task.enums.TaskStatus;
import ru.kstn.taskmanagementsystem.user.dto.SimpleUserDto;

import java.util.List;

@Data
public class ResponseTaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private SimpleUserDto creator;
    private List<SimpleUserDto> performerList;
    private List<SimpleCommentDto> commentList;
}
