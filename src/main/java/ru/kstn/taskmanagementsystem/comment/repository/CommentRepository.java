package ru.kstn.taskmanagementsystem.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kstn.taskmanagementsystem.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTaskId(Long taskId);
}
