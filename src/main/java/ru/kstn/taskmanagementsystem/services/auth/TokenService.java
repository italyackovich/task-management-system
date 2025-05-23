package ru.kstn.taskmanagementsystem.services.auth;

import ru.kstn.taskmanagementsystem.model.entity.User;

public interface TokenService {
    void saveUserToken(User user, String refreshToken);
    void revokeAllUserTokens(User user);
}
