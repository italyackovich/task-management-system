package ru.kstn.taskmanagementsystem.entities.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.kstn.taskmanagementsystem.entities.comment.Comment;
import ru.kstn.taskmanagementsystem.entities.task.Task;
import ru.kstn.taskmanagementsystem.enums.user.UserRole;

import java.util.List;

@Entity
@Setter
@Getter
@EqualsAndHashCode(of = {"username", "email"})
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> createdTaskList;

    @ManyToMany(mappedBy = "performerList")
    private List<Task> performedTaskList;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;
}
