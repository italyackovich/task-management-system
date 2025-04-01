package ru.kstn.taskmanagementsystem.auth.exception;

public class MissingCookieException extends RuntimeException {
    public MissingCookieException(String message) {
        super(message);
    }
}
