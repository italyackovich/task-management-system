package ru.kstn.taskmanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.kstn.taskmanagementsystem.model.enums.UserRole;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User extends BaseEntity {

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
