package com.example.user_service.Errors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.user_service.Errors.custom.InvalidArgumentException;
import com.example.user_service.Errors.custom.InvalidJwtTokenException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<?> handleInvalidJwt(InvalidJwtTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidArgument(
            InvalidArgumentException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid Argument");
        response.put("message", ex.getMessage());
        response.put("fieldErrors", ex.getFieldErrors());
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
