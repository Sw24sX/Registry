package com.example.registry.dto;

import lombok.Data;

@Data
public class UserDataDto {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
}
