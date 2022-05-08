package com.example.registry.service.persistance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyExist extends RegistryException {
    public EntityAlreadyExist() {
        super("Entity is already exist");
    }
}
