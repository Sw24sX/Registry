package com.example.registry.service;

import com.example.registry.service.persistance.Routing;

public interface MessagingServiceFacade {
    <T, R> T doRequest(R requestBody, Routing routing, Class<T> responseType);
}
