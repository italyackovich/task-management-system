package ru.kstn.taskmanagementsystem.services.user;

import ru.kstn.taskmanagementsystem.dtos.user.RequestUserDto;
import ru.kstn.taskmanagementsystem.dtos.user.ResponseUserDto;

import java.util.List;

public interface UserService {
    List<ResponseUserDto> getUserList();
    ResponseUserDto getUserById(Long id);
    ResponseUserDto createUser(RequestUserDto request);
    ResponseUserDto updateUser(Long id, RequestUserDto request);
    void deleteUser(Long id);
}
