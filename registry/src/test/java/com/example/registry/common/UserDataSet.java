package com.example.registry.common;

import com.example.registry.model.UserData;

public class UserDataSet {
    private UserDataSet() {
    }

    public static UserData createValid() {
        return new UserData("test", "213", "username@domain.com", "test", true);
    }

    public static UserData createInvalid() {
        return new UserData("@#", "213", "test123", "%^&", true);
    }
}
