package ru.kstn.taskmanagementsystem.dtos.comment;

import lombok.Data;
import ru.kstn.taskmanagementsystem.dtos.task.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.dtos.user.SimpleUserDto;

@Data
public class ResponseCommentDto {
    private Long id;
    private String content;
    private SimpleUserDto creator;
    private SimpleTaskDto task;
}
