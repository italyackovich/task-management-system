package ru.kstn.taskmanagementsystem.comment.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.kstn.taskmanagementsystem.task.entity.Task;
import ru.kstn.taskmanagementsystem.user.entity.User;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
