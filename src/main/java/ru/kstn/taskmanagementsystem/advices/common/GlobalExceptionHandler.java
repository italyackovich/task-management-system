package ru.kstn.taskmanagementsystem.advices.common;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kstn.taskmanagementsystem.exceptions.auth.InvalidTokenException;
import ru.kstn.taskmanagementsystem.exceptions.auth.MissingCookieException;
import ru.kstn.taskmanagementsystem.exceptions.auth.RevokedTokenException;
import ru.kstn.taskmanagementsystem.exceptions.auth.TokenNotFoundException;
import ru.kstn.taskmanagementsystem.exceptions.comment.CommentCreatorNotPerformerException;
import ru.kstn.taskmanagementsystem.exceptions.comment.CommentNotFoundException;
import ru.kstn.taskmanagementsystem.dtos.common.ErrorResponseDto;
import ru.kstn.taskmanagementsystem.exceptions.task.TaskNotFoundException;
import ru.kstn.taskmanagementsystem.exceptions.user.UserNotFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto("User not found", List.of(exception.getMessage())));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleTaskNotFoundException(TaskNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto("Task not found", List.of(exception.getMessage())));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCommentNotFoundException(CommentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto("Comment not found", List.of(exception.getMessage())));
    }

    @ExceptionHandler(CommentCreatorNotPerformerException.class)
    public ResponseEntity<ErrorResponseDto> handleCommentCreatorNotPerformerException(CommentCreatorNotPerformerException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto("Comment creator not performer", List.of(exception.getMessage())));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidTokenException(InvalidTokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Invalid token", List.of(exception.getMessage())));
    }

    @ExceptionHandler(MissingCookieException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingCookieException(MissingCookieException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Missing cookies", List.of(exception.getMessage())));
    }

    @ExceptionHandler(RevokedTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleRevokedTokenException(RevokedTokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Revoked token", List.of(exception.getMessage())));
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleTokenNotFoundException(TokenNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto("Token not found", List.of(exception.getMessage())));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseDto> handleExpiredJwtException(ExpiredJwtException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto("Expired token", List.of(exception.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto("Invalid request", errors));
    }
}
