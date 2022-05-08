package com.example.registry.service;

import com.example.registry.service.persistance.Routing;
import com.example.registry.service.persistance.message.UserDataMessage;

public interface MessagingServiceFacade {
    <T, R> T doRequest(R requestBody, Routing routing, Class<T> responseType);

    Boolean requestToStub(UserDataMessage message);
}
