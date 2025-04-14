package ru.kstn.taskmanagementsystem.entities.task;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.kstn.taskmanagementsystem.entities.comment.Comment;
import ru.kstn.taskmanagementsystem.enums.task.TaskPriority;
import ru.kstn.taskmanagementsystem.enums.task.TaskStatus;
import ru.kstn.taskmanagementsystem.entities.user.User;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"title", "creator"})
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
