package ru.kstn.taskmanagementsystem.exceptions.auth;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
