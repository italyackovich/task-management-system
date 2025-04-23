package ru.kstn.taskmanagementsystem.repositories.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kstn.taskmanagementsystem.model.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskId(Long taskId);
}
