package ru.kstn.taskmanagementsystem.user.service;

import ru.kstn.taskmanagementsystem.user.dto.RequestUserDto;
import ru.kstn.taskmanagementsystem.user.dto.ResponseUserDto;

import java.util.List;

public interface UserService {
    List<ResponseUserDto> getUserList();
    ResponseUserDto getUserById(Long id);
    ResponseUserDto createUser(RequestUserDto request);
    ResponseUserDto updateUser(Long id, RequestUserDto request);
    void deleteUser(Long id);
}
