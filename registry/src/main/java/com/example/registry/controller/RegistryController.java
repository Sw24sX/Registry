package com.example.registry.controller;

import com.example.registry.dto.UserDataRequest;
import com.example.registry.service.UserDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registry")
public class RegistryController {
    private final UserDataService userDataService;

    public RegistryController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @PostMapping
    public UserDataRequest registry(@RequestBody UserDataRequest userData) throws JsonProcessingException {
        return userDataService.create(userData);
    }
}
