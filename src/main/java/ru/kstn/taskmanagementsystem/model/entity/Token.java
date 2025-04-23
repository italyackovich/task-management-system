package ru.kstn.taskmanagementsystem.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Boolean isRevoked;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
