package ru.kstn.taskmanagementsystem.user.dto;

import lombok.Data;
import ru.kstn.taskmanagementsystem.user.enums.UserRole;

@Data
public class SimpleUserDto {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
}
