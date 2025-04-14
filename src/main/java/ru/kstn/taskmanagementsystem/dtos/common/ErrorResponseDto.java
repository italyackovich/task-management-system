package ru.kstn.taskmanagementsystem.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
    private String error;
    private List<String> message;
}
