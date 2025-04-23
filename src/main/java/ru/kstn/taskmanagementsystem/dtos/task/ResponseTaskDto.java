package ru.kstn.taskmanagementsystem.dtos.task;

import lombok.Data;
import ru.kstn.taskmanagementsystem.dtos.comment.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.model.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.model.enums.TaskStatus;
import ru.kstn.taskmanagementsystem.dtos.user.SimpleUserDto;

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
