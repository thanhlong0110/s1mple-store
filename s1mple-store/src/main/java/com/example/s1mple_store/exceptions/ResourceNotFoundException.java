package com.example.s1mple_store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for handling resource not found errors.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     * 
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}