package ru.kstn.taskmanagementsystem.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.auth.security.CustomUserDetails;
import ru.kstn.taskmanagementsystem.user.exception.UserNotFoundException;
import ru.kstn.taskmanagementsystem.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return new CustomUserDetails(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found")));
    }
}
