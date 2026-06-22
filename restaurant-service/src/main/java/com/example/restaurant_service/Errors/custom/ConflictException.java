package com.example.restaurant_service.Errors.custom;

/**
 * Thrown when a request is well-formed and authorized but conflicts with the
 * current state of a resource (mapped to HTTP 409). Example: deleting a menu
 * category that still has menu items.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
