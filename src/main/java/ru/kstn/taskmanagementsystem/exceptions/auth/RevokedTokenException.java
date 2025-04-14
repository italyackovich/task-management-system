package ru.kstn.taskmanagementsystem.exceptions.auth;

public class RevokedTokenException extends RuntimeException {
    public RevokedTokenException(String message) {
        super(message);
    }
}
