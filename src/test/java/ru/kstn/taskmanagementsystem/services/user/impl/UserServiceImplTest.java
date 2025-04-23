package ru.kstn.taskmanagementsystem.services.user.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kstn.taskmanagementsystem.dtos.user.RequestUserDto;
import ru.kstn.taskmanagementsystem.dtos.user.ResponseUserDto;
import ru.kstn.taskmanagementsystem.model.entity.User;
import ru.kstn.taskmanagementsystem.exceptions.user.UserNotFoundException;
import ru.kstn.taskmanagementsystem.mappers.user.UserMapper;
import ru.kstn.taskmanagementsystem.repositories.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserList() {
        User user1 = new User();
        User user2 = new User();
        ResponseUserDto dto1 = new ResponseUserDto();
        ResponseUserDto dto2 = new ResponseUserDto();

        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserDto(user1)).thenReturn(dto1);
        when(userMapper.toUserDto(user2)).thenReturn(dto2);

        List<ResponseUserDto> result = userService.getUserList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void testGetUserById_UserExists() {
        Long userId = 1L;
        User user = new User();
        ResponseUserDto dto = new ResponseUserDto();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(dto);

        ResponseUserDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void testGetUserById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
        assertTrue(ex.getMessage().contains("User with id " + userId + " not found"));
    }

    @Test
    void testCreateUser() {
        RequestUserDto request = new RequestUserDto();
        request.setPassword("plainPassword");

        User user = new User();
        user.setPassword("plainPassword");

        ResponseUserDto dto = new ResponseUserDto();

        when(userMapper.toUser(request)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(dto);

        ResponseUserDto result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).save(user);
        assertEquals(dto, result);
    }


    @Test
    void testUpdateUser_UserExists() {
        Long userId = 1L;
        RequestUserDto request = new RequestUserDto();
        User existingUser = new User();
        User updatedUser = new User();
        ResponseUserDto dto = new ResponseUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toUser(request)).thenReturn(updatedUser);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(dto);

        ResponseUserDto result = userService.updateUser(userId, request);

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(updatedUser);
        assertEquals(dto, result);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        Long userId = 1L;
        RequestUserDto request = new RequestUserDto();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(userId, request);
        });
        assertTrue(ex.getMessage().contains("User with id " + userId + " not found"));
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
}
