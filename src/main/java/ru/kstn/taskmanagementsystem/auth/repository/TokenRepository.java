package ru.kstn.taskmanagementsystem.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kstn.taskmanagementsystem.auth.entity.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUserIdAndIsRevokedFalse(Long id);
    Optional<Token> findByToken(String token);
}
