package com.example.registry.controller;

import com.example.registry.dto.UserDataDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registry")
public class RegistryController {
    @PostMapping
    public UserDataDto registry(@RequestBody UserDataDto userDataDto) {
        return null;
    }
}
