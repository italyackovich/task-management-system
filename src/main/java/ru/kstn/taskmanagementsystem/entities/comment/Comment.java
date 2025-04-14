package ru.kstn.taskmanagementsystem.entities.comment;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.kstn.taskmanagementsystem.entities.task.Task;
import ru.kstn.taskmanagementsystem.entities.user.User;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
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
