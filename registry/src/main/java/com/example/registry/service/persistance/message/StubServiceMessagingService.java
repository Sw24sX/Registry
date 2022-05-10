package com.example.registry.service.persistance.message;

import com.example.registry.service.persistance.message.dto.UserDataMessage;

public interface StubServiceMessagingService {
    Boolean registryUser(UserDataMessage userData);
}
