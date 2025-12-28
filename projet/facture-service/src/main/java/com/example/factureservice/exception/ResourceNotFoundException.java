package com.example.factureservice.exception;

/**
 * @author sawssan
 **/
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
