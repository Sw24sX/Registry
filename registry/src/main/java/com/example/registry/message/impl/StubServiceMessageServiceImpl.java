package com.example.registry.message.impl;

import com.example.registry.message.StubServiceMessagingService;
import com.example.registry.message.dto.Message;
import com.example.registry.message.dto.UserDataMessage;
import com.example.registry.message.messaging.MessagingService;
import com.example.registry.service.persistance.exception.RegistryException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
public class StubServiceMessageServiceImpl implements StubServiceMessagingService {
    private final MessagingService booleanResponseMessaging;

    public StubServiceMessageServiceImpl(@Qualifier("booleanResponse") MessagingService booleanResponseMessaging) {
        this.booleanResponseMessaging = booleanResponseMessaging;
    }

    @Override
    public Boolean registryUser(UserDataMessage userData) {
        Message<UserDataMessage> message = new Message<>(userData, "stub-service");
        try {
            Message<Boolean> responseMessage = booleanResponseMessaging.doRequest(message);
            return responseMessage.getBody();
        } catch (TimeoutException e) {
            throw new RegistryException(e);
        }
    }
}
