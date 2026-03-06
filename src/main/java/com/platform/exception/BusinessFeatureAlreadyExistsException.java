package com.platform.exception;

public class BusinessFeatureAlreadyExistsException extends RuntimeException {
    public BusinessFeatureAlreadyExistsException(String message) {
        super(message);
    }
}
