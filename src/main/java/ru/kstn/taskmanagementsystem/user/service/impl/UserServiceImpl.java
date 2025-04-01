package ru.kstn.taskmanagementsystem.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.user.dto.RequestUserDto;
import ru.kstn.taskmanagementsystem.user.dto.ResponseUserDto;
import ru.kstn.taskmanagementsystem.user.entity.User;
import ru.kstn.taskmanagementsystem.user.exception.UserNotFoundException;
import ru.kstn.taskmanagementsystem.user.mapper.UserMapper;
import ru.kstn.taskmanagementsystem.user.repository.UserRepository;
import ru.kstn.taskmanagementsystem.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ResponseUserDto> getUserList() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).toList();
    }

    @Override
    public ResponseUserDto getUserById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
    }

    @Override
    public ResponseUserDto createUser(RequestUserDto request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public ResponseUserDto updateUser(Long id, RequestUserDto request) {
        return userRepository.findById(id).map(oldUser -> {
            User user = userMapper.toUser(request);
            user.setId(oldUser.getId());
            return userMapper.toUserDto(userRepository.save(user));
        }).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
