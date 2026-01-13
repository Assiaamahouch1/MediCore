package com.example.auth_service.exception;

/**
 * @author sawssan
 **/
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
