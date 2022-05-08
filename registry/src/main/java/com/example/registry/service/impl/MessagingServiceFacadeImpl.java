package com.example.registry.service.impl;

import com.example.registry.service.MessagingService;
import com.example.registry.service.MessagingServiceFacade;
import com.example.registry.service.persistance.Routing;
import com.example.registry.service.persistance.exception.RegistryException;
import com.example.registry.service.persistance.message.Message;
import com.example.registry.service.persistance.message.UserDataMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
public class MessagingServiceFacadeImpl implements MessagingServiceFacade {
    private final ObjectMapper objectMapper;
    private final MessagingService messagingService;

    public MessagingServiceFacadeImpl(ObjectMapper objectMapper, MessagingService messagingService) {
        this.objectMapper = objectMapper;
        this.messagingService = messagingService;
    }

    @Override
    public <T, R> T doRequest(R requestBody, Routing routing, Class<T> responseType) {
        try {
            Message<Object> response = messagingService.doRequest(new Message<>(requestBody, routing));
            return objectMapper.convertValue(response.getBody(), responseType);
        } catch (TimeoutException e) {
            throw new RegistryException(e.getMessage());
        }
    }

    @Override
    public Boolean requestToStub(UserDataMessage message) {
        return doRequest(message, Routing.REGISTRY, Boolean.class);
    }
}
