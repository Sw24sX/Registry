package com.example.registry.service;

import com.example.registry.service.persistance.Message;

public interface MessageListener<T> {
    void handleMessage(Message<T> incomingMessage);
}
