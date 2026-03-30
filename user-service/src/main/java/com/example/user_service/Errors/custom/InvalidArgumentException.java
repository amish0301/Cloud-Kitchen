package com.example.user_service.Errors.custom;

import java.util.Map;

public class InvalidArgumentException extends IllegalArgumentException {
    
    private final Map<String, String> fieldErrors;

    public InvalidArgumentException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}

