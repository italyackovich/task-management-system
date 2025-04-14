package ru.kstn.taskmanagementsystem.services.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.kstn.taskmanagementsystem.entities.auth.CustomUserDetails;
import ru.kstn.taskmanagementsystem.exceptions.user.UserNotFoundException;
import ru.kstn.taskmanagementsystem.repositories.user.UserRepository;

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
