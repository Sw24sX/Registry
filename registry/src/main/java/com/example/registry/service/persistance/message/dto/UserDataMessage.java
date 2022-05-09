package com.example.registry.service.persistance.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDataMessage {
    private String id;
    private String name;
    private String login;
    private String password;
    private String email;
    private boolean isApproval;
}
