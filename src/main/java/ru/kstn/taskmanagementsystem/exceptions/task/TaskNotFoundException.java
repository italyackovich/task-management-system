package ru.kstn.taskmanagementsystem.exceptions.task;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
