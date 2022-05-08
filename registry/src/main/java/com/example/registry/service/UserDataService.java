package com.example.registry.service;

import com.example.registry.model.UserData;

public interface UserDataService {
    UserData create(UserData userData);
    void flush();
}
