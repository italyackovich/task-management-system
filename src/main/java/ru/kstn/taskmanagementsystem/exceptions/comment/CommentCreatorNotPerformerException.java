package ru.kstn.taskmanagementsystem.exceptions.comment;

public class CommentCreatorNotPerformerException extends RuntimeException {
    public CommentCreatorNotPerformerException(String message) {
        super(message);
    }
}
