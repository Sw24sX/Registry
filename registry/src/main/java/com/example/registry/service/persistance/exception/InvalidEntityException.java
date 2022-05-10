package com.example.registry.service.persistance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEntityException extends RegistryException {
    private static final long serialVersionUID = -4275535137545875681L;

    public InvalidEntityException() {
        super("Entity is invalid");
    }
}
