package ru.kstn.taskmanagementsystem.task.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import ru.kstn.taskmanagementsystem.user.entity.User;
import ru.kstn.taskmanagementsystem.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @Mock private TaskMapper taskMapper;
    @Mock private SimpleTaskMapper simpleTaskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testGetTaskListByCreatorUsername() {
        String username = "creator";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = List.of(task1, task2);
        when(taskRepository.findTaskByCreator_Username(username, pageable)).thenReturn(tasks);
        SimpleTaskDto dto1 = new SimpleTaskDto();
        SimpleTaskDto dto2 = new SimpleTaskDto();
        when(simpleTaskMapper.toSimpleTaskDto(task1)).thenReturn(dto1);
        when(simpleTaskMapper.toSimpleTaskDto(task2)).thenReturn(dto2);

        List<SimpleTaskDto> result = taskService.getTaskListByCreatorUsername(username, pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void testGetTaskListByPerformerUsername() {
        String username = "performer";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Task task = new Task();
        List<Task> tasks = List.of(task);
        when(taskRepository.findTaskByPerformerList_Username(username, pageable)).thenReturn(tasks);
        SimpleTaskDto dto = new SimpleTaskDto();
        when(simpleTaskMapper.toSimpleTaskDto(task)).thenReturn(dto);

        List<SimpleTaskDto> result = taskService.getTaskListByPerformerUsername(username, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(dto));
    }

    @Test
    void testGetTaskById_TaskExists() {
        Long taskId = 1L;
        Task task = new Task();
        ResponseTaskDto dto = new ResponseTaskDto();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toResponseTaskDto(task)).thenReturn(dto);

        ResponseTaskDto result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void testGetTaskById_TaskNotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
        assertTrue(ex.getMessage().contains("Task with id " + taskId + " not found"));
    }

    @Test
    void testCreateTask() {
        RequestTaskDto request = new RequestTaskDto();
        request.setCreatorId(1L);
        request.setPerformerId(2L);

        Task task = new Task();
        when(taskMapper.toTask(request)).thenReturn(task);

        User creator = new User();
        User performer = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(userRepository.findById(2L)).thenReturn(Optional.of(performer));

        when(taskRepository.save(task)).thenReturn(task);

        ResponseTaskDto dto = new ResponseTaskDto();
        when(taskMapper.toResponseTaskDto(task)).thenReturn(dto);

        ResponseTaskDto result = taskService.createTask(request);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(taskRepository).save(task);
        assertEquals(dto, result);
    }

    @Test
    void testUpdateTask_TaskExists() {
        Long taskId = 1L;
        RequestTaskDto request = new RequestTaskDto();
        request.setCreatorId(1L);
        request.setPerformerId(2L);

        Task oldTask = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        Task task = new Task();
        when(taskMapper.toTask(request)).thenReturn(task);

        User creator = new User();
        User performer = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(userRepository.findById(2L)).thenReturn(Optional.of(performer));

        when(taskRepository.save(task)).thenReturn(task);
        ResponseTaskDto dto = new ResponseTaskDto();
        when(taskMapper.toResponseTaskDto(task)).thenReturn(dto);

        ResponseTaskDto result = taskService.updateTask(taskId, request);

        assertNotNull(result);
        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(taskRepository).save(task);
        assertEquals(dto, result);
    }

    @Test
    void testUpdateTask_TaskNotFound() {
        Long taskId = 1L;
        RequestTaskDto request = new RequestTaskDto();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskId, request));
        assertTrue(ex.getMessage().contains("Task with id " + taskId + " not found"));
    }

    @Test
    void testUpdatePerformerToList_TaskExists() {
        Long taskId = 1L;
        Long performerId = 2L;
        Task task = new Task();
        List<User> performerList = new ArrayList<>();
        task.setPerformerList(performerList);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        User performer = new User();
        when(userRepository.findById(performerId)).thenReturn(Optional.of(performer));

        when(taskRepository.save(task)).thenReturn(task);
        ResponseTaskDto dto = new ResponseTaskDto();
        when(taskMapper.toResponseTaskDto(task)).thenReturn(dto);

        ResponseTaskDto result = taskService.updatePerformerToList(taskId, performerId);

        assertNotNull(result);
        assertTrue(task.getPerformerList().contains(performer));
        verify(userRepository).findById(performerId);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
        assertEquals(dto, result);
    }

    @Test
    void testUpdatePerformerToList_TaskNotFound() {
        Long taskId = 999L;
        Long performerId = 2L;

        User performer = new User();
        when(userRepository.findById(performerId)).thenReturn(Optional.of(performer));

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.updatePerformerToList(taskId, performerId));
        assertTrue(ex.getMessage().contains("Task with id " + taskId + " not found"));
    }


    @Test
    void testChangeStatus_TaskExists() {
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.FINISHED;
        Task task = new Task();
        task.setStatus(TaskStatus.RUNNING);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        when(taskRepository.save(task)).thenReturn(task);
        ResponseTaskDto dto = new ResponseTaskDto();
        when(taskMapper.toResponseTaskDto(task)).thenReturn(dto);

        ResponseTaskDto result = taskService.changeStatus(taskId, newStatus);

        assertNotNull(result);
        assertEquals(newStatus, task.getStatus());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
        assertEquals(dto, result);
    }

    @Test
    void testChangeStatus_TaskNotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.changeStatus(taskId, TaskStatus.FINISHED));
        assertTrue(ex.getMessage().contains("Task with id " + taskId + " not found"));
    }

    @Test
    void testChangePriority_TaskExists() {
        Long taskId = 1L;
        TaskPriority newPriority = TaskPriority.HIGH;
        Task task = new Task();
        task.setPriority(TaskPriority.LOW);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        when(taskRepository.save(task)).thenReturn(task);
        ResponseTaskDto dto = new ResponseTaskDto();
        when(taskMapper.toResponseTaskDto(task)).thenReturn(dto);

        ResponseTaskDto result = taskService.changePriority(taskId, newPriority);

        assertNotNull(result);
        assertEquals(newPriority, task.getPriority());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
        assertEquals(dto, result);
    }

    @Test
    void testChangePriority_TaskNotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.changePriority(taskId, TaskPriority.HIGH));
        assertTrue(ex.getMessage().contains("Task with id " + taskId + " not found"));
    }

    @Test
    void testDeleteTaskById() {
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTaskById(taskId);

        verify(taskRepository).deleteById(taskId);
    }
}
