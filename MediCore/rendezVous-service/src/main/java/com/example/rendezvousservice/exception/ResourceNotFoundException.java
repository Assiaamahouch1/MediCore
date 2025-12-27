package com.example.rendezvousservice.exception;

/**
 * @author sawssan
 **/
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
