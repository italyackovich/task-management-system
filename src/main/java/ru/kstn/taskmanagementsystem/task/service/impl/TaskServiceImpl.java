package ru.kstn.taskmanagementsystem.task.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.task.dto.RequestTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.ResponseTaskDto;
import ru.kstn.taskmanagementsystem.task.dto.SimpleTaskDto;
import ru.kstn.taskmanagementsystem.task.entity.Task;
import ru.kstn.taskmanagementsystem.task.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.task.enums.TaskStatus;
import ru.kstn.taskmanagementsystem.task.exception.TaskNotFoundException;
import ru.kstn.taskmanagementsystem.task.mapper.SimpleTaskMapper;
import ru.kstn.taskmanagementsystem.task.mapper.TaskMapper;
import ru.kstn.taskmanagementsystem.task.repository.TaskRepository;
import ru.kstn.taskmanagementsystem.task.service.TaskService;
import ru.kstn.taskmanagementsystem.user.entity.User;
import ru.kstn.taskmanagementsystem.user.exception.UserNotFoundException;
import ru.kstn.taskmanagementsystem.user.repository.UserRepository;

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
