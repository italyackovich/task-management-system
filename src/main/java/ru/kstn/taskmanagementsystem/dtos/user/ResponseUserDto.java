package ru.kstn.taskmanagementsystem.dtos.user;

import lombok.Data;
import ru.kstn.taskmanagementsystem.dtos.comment.SimpleCommentDto;
import ru.kstn.taskmanagementsystem.dtos.task.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.enums.user.UserRole;

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
