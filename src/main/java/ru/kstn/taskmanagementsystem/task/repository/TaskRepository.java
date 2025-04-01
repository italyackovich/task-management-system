package ru.kstn.taskmanagementsystem.task.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kstn.taskmanagementsystem.task.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByCreator_Username(String creatorUsername, Pageable pageable);
    List<Task> findTaskByPerformerList_Username(String performerUsername, Pageable pageable);
}
