package ru.kstn.taskmanagementsystem.services.task.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.dtos.task.RequestTaskDto;
import ru.kstn.taskmanagementsystem.dtos.task.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.dtos.task.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.entities.task.Task;
import ru.kstn.taskmanagementsystem.enums.task.TaskPriority;
import ru.kstn.taskmanagementsystem.enums.task.TaskStatus;
import ru.kstn.taskmanagementsystem.exceptions.task.TaskNotFoundException;
import ru.kstn.taskmanagementsystem.mappers.task.SimpleTaskMapper;
import ru.kstn.taskmanagementsystem.mappers.task.TaskMapper;
import ru.kstn.taskmanagementsystem.repositories.task.TaskRepository;
import ru.kstn.taskmanagementsystem.services.task.TaskService;
import ru.kstn.taskmanagementsystem.entities.user.User;
import ru.kstn.taskmanagementsystem.exceptions.user.UserNotFoundException;
import ru.kstn.taskmanagementsystem.repositories.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final SimpleTaskMapper simpleTaskMapper;

    @Override
    public List<SimpleTaskDto> getTaskListByCreatorUsername(String username, Pageable pageable) {
        return taskRepository.findTaskByCreator_Username(username, pageable).stream().map(simpleTaskMapper::toSimpleTaskDto).toList();
    }

    @Override
    public List<SimpleTaskDto> getTaskListByPerformerUsername(String username, Pageable pageable) {
        return taskRepository.findTaskByPerformerList_Username(username, pageable).stream().map(simpleTaskMapper::toSimpleTaskDto).toList();
    }

    @Override
    public ResponseTaskDto getTaskById(Long id) {
        return taskMapper.toResponseTaskDto(taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found")));
    }

    @Override
    public ResponseTaskDto createTask(RequestTaskDto request) {
        Task task = taskMapper.toTask(request);
        User creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("Creator with id " + request.getCreatorId() + " not found"));
        User performer = userRepository.findById(request.getPerformerId())
                .orElseThrow(() -> new UserNotFoundException("Performer with id " + request.getPerformerId() + " not found"));
        task.setCreator(creator);
        task.setPerformerList(List.of(performer));
        return taskMapper.toResponseTaskDto(taskRepository.save(task));
    }

    @Override
    public ResponseTaskDto updateTask(Long id, RequestTaskDto request) {
        return taskRepository.findById(id).map(oldTask -> {
            Task task = taskMapper.toTask(request);
            User creator = userRepository.findById(request.getCreatorId())
                    .orElseThrow(() -> new UserNotFoundException("Creator with id " + request.getCreatorId() + " not found"));

            User performer = userRepository.findById(request.getPerformerId())
                    .orElseThrow(() -> new UserNotFoundException("Performer with id " + request.getPerformerId() + " not found"));
            task.setId(oldTask.getId());
            task.setCreator(creator);
            task.setPerformerList(List.of(performer));
            return taskMapper.toResponseTaskDto(taskRepository.save(task));
        }).orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public ResponseTaskDto updatePerformerToList(Long taskId, Long performerId) {
        User performer = userRepository.findById(performerId)
                .orElseThrow(() -> new UserNotFoundException("Performer with id " + performerId + " not found"));
        return taskRepository.findById(taskId).map(task -> {
            List<User> performerList = task.getPerformerList();
            performerList.add(performer);
            task.setPerformerList(performerList);
            return taskMapper.toResponseTaskDto(taskRepository.save(task));
        }).orElseThrow(() -> new TaskNotFoundException("Task with id " + taskId + " not found"));
    }

    @Override
    public ResponseTaskDto changeStatus(Long id, TaskStatus status) {
        return taskRepository.findById(id).map(task -> {
            task.setStatus(status);
            return taskMapper.toResponseTaskDto(taskRepository.save(task));
        }).orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public ResponseTaskDto changePriority(Long id, TaskPriority priority) {
        return taskRepository.findById(id).map(task -> {
            task.setPriority(priority);
            return taskMapper.toResponseTaskDto(taskRepository.save(task));
        }).orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
