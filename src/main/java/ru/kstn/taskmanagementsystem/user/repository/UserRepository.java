package ru.kstn.taskmanagementsystem.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kstn.taskmanagementsystem.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
