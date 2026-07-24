package com.tradingplatformByTrove.common.exception;

/**
 * Exception thrown when a requested resource is not present.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}