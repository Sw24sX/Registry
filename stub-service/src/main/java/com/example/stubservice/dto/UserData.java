package com.example.stubservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String id;
    private String name;
    private String password;
    private String email;
    private String login;
    private boolean isApproval;
}
