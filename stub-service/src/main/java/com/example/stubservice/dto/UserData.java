package com.example.stubservice.dto;

import lombok.Data;

@Data
public class UserData {
    private Long id;
    private String name;
    private String login;
    private String email;
    private boolean isApproved;
}
