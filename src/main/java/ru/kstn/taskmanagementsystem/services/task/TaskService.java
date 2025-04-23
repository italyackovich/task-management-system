package ru.kstn.taskmanagementsystem.services.task;

import org.springframework.data.domain.Pageable;
import ru.kstn.taskmanagementsystem.dtos.task.RequestTaskDto;
import ru.kstn.taskmanagementsystem.dtos.task.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.dtos.task.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.model.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.model.enums.TaskStatus;

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
