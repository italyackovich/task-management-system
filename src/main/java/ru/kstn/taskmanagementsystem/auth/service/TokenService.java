package ru.kstn.taskmanagementsystem.auth.service;

import ru.kstn.taskmanagementsystem.user.entity.User;

public interface TokenService {
    void saveUserToken(User user, String refreshToken);
    void revokeAllUserTokens(User user);
}
