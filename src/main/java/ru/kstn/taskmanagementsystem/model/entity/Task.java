package ru.kstn.taskmanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.kstn.taskmanagementsystem.model.enums.TaskPriority;
import ru.kstn.taskmanagementsystem.model.enums.TaskStatus;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class Task extends BaseEntity {

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
