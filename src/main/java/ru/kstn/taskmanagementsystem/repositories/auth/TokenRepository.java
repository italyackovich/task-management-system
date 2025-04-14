package ru.kstn.taskmanagementsystem.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kstn.taskmanagementsystem.entities.auth.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUserIdAndIsRevokedFalse(Long id);
    Optional<Token> findByToken(String token);
}
