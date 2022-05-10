package com.example.registry.service.persistance.exception;

public class RegistryException extends RuntimeException {
    private static final long serialVersionUID = -2912422976824589381L;

    public RegistryException(String message) {
        super(message);
    }

    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistryException(Exception exception) {
        super(exception.getMessage(), exception.getCause());
    }
}
