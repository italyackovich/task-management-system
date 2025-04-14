package ru.kstn.taskmanagementsystem.exceptions.auth;

public class MissingCookieException extends RuntimeException {
    public MissingCookieException(String message) {
        super(message);
    }
}
