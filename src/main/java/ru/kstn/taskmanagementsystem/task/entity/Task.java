package ru.kstn.taskmanagementsystem.task.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.kstn.taskmanagementsystem.comment.entity.Comment;
import ru.kstn.taskmanagementsystem.task.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.task.enums.TaskStatus;
import ru.kstn.taskmanagementsystem.user.entity.User;

import java.util.List;

@Entity
@Data
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "performer_task",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "performer_id"))
    private List<User> performerList;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;
}
