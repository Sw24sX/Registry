package com.example.registry.controller;

import com.example.registry.controller.dto.UserDataRequest;
import com.example.registry.service.RegistryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registry")
public class RegistryController {
    private final RegistryService registryService;

    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostMapping
    public UserDataRequest registry(@RequestBody UserDataRequest userData) throws JsonProcessingException {
        return registryService.registry(userData);
    }
}
