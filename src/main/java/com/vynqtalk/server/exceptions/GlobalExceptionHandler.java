package com.vynqtalk.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vynqtalk.server.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        System.out.println(ex.getBindingResult().getFieldErrors());
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Invalid data");
        return ResponseEntity.ok(new ApiResponse<>(false, null, errorMessage));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, null, "Verification failed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, "Internal server error: " + ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, null,"User not found"));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, null,ex.getMessage()));
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleGroupNotFoundException(GroupNotFoundException ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, null,"Group not found"));
    }

    @ExceptionHandler(GroupMessageNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleGroupMessageNotFoundException(GroupMessageNotFoundException ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, null,"Message not found"));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, null,"Notification not found"));
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleMessageNotFoundException(MessageNotFoundException ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, null,"Message not found"));
    }

    @ExceptionHandler(UserSettingsNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserSettingsNotFoundException(UserSettingsNotFoundException ex) {
        return ResponseEntity.ok(new ApiResponse<>(false, null,"User settings not found"));
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiResponse<?>> handleSystemExceptions(SystemException ex){
        return ResponseEntity.ok(new ApiResponse<>(false,null,ex.getMessage()));
    }
}