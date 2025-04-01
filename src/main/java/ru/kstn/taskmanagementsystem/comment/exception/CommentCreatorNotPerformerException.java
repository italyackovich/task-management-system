package ru.kstn.taskmanagementsystem.comment.exception;

public class CommentCreatorNotPerformerException extends RuntimeException {
    public CommentCreatorNotPerformerException(String message) {
        super(message);
    }
}
