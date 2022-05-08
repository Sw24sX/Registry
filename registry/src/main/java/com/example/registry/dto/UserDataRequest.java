package com.example.registry.dto;

import lombok.Data;

@Data
public class UserDataRequest {
    private String id;
    private String name;
    private String login;
    private String password;
    private String email;
    private boolean isApproval;
}
