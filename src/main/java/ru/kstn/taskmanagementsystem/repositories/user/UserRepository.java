package ru.kstn.taskmanagementsystem.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kstn.taskmanagementsystem.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
