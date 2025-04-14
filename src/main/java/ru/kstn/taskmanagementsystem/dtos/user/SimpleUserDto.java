package ru.kstn.taskmanagementsystem.dtos.user;

import lombok.Data;
import ru.kstn.taskmanagementsystem.enums.user.UserRole;

@Data
public class SimpleUserDto {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
}
