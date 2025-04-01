package ru.kstn.taskmanagementsystem.user.dto;

import lombok.Data;
import ru.kstn.taskmanagementsystem.comment.dto.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.comment.entity.Comment;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.entity.Task;
import ru.kstn.taskmanagementsystem.user.enums.UserRole;

import java.util.List;

@Data
public class ResponseUserDto {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private List<SimpleTaskDto> createdTaskList;
    private List<SimpleTaskDto> performedTaskList;
    private List<SimpleCommentDto> commentList;
}
