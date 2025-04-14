package ru.kstn.taskmanagementsystem.exceptions.auth;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
