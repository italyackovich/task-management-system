package ru.kstn.taskmanagementsystem.services.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kstn.taskmanagementsystem.model.entity.Token;
import ru.kstn.taskmanagementsystem.repositories.auth.TokenRepository;
import ru.kstn.taskmanagementsystem.services.auth.TokenService;
import ru.kstn.taskmanagementsystem.model.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveUserToken(User user, String refreshToken) {
        Token token = Token.builder()
                .token(refreshToken)
                .user(user)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user) {
        stringRedisTemplate.delete(user.getEmail());
        List<Token> validUserTokens = tokenRepository.findByUserIdAndIsRevokedFalse(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token ->
                token.setIsRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }
}
