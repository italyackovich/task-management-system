package ru.kstn.taskmanagementsystem.task.service;

import org.springframework.data.domain.Pageable;
import ru.kstn.taskmanagementsystem.task.dto.RequestTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.task.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    List<SimpleTaskDto> getTaskListByCreatorUsername(String username, Pageable pageable);
    List<SimpleTaskDto> getTaskListByPerformerUsername(String username, Pageable pageable);
    ResponseTaskDto getTaskById(Long id);
    ResponseTaskDto createTask(RequestTaskDto request);
    ResponseTaskDto updateTask(Long id, RequestTaskDto request);
    ResponseTaskDto updatePerformerToList(Long taskId, Long performerId);
    ResponseTaskDto changeStatus(Long id, TaskStatus status);
    ResponseTaskDto changePriority(Long id, TaskPriority priority);
    void deleteTaskById(Long id);
}
