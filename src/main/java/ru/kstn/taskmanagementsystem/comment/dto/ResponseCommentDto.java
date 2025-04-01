package ru.kstn.taskmanagementsystem.comment.dto;

import lombok.Data;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.user.dto.SimpleUserDto;

@Data
public class ResponseCommentDto {
    private Long id;
    private String content;
    private SimpleUserDto creator;
    private SimpleTaskDto task;
}
