package com.example.registry.message;

import com.example.registry.message.dto.UserDataMessage;

public interface StubServiceMessagingService {
    Boolean registryUser(UserDataMessage userData);
}
