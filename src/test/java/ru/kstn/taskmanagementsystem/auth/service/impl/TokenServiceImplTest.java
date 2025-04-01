package ru.kstn.taskmanagementsystem.auth.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import ru.kstn.taskmanagementsystem.auth.entity.Token;
import ru.kstn.taskmanagementsystem.auth.repository.TokenRepository;
import ru.kstn.taskmanagementsystem.user.entity.User;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Test
    void testSaveUserToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        String refreshToken = "refreshToken123";

        tokenService.saveUserToken(user, refreshToken);

        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        Token capturedToken = tokenCaptor.getValue();
        assertEquals(refreshToken, capturedToken.getToken());
        assertEquals(user, capturedToken.getUser());
        assertFalse(capturedToken.getIsRevoked());
    }

    @Test
    void testRevokeAllUserTokens_NoValidTokens() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(tokenRepository.findByUserIdAndIsRevokedFalse(user.getId())).thenReturn(Collections.emptyList());

        tokenService.revokeAllUserTokens(user);

        verify(stringRedisTemplate).delete(user.getEmail());
        verify(tokenRepository, never()).saveAll(anyList());
    }

    @Test
    void testRevokeAllUserTokens_WithValidTokens() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        Token token1 = Token.builder()
                .token("token1")
                .user(user)
                .isRevoked(false)
                .build();
        Token token2 = Token.builder()
                .token("token2")
                .user(user)
                .isRevoked(false)
                .build();
        List<Token> validTokens = List.of(token1, token2);
        when(tokenRepository.findByUserIdAndIsRevokedFalse(user.getId())).thenReturn(validTokens);

        tokenService.revokeAllUserTokens(user);

        verify(stringRedisTemplate).delete(user.getEmail());

        for (Token token : validTokens) {
            assertTrue(token.getIsRevoked());
        }
        verify(tokenRepository).saveAll(validTokens);
    }
}
