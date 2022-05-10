package com.example.registry.service.persistance.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message<T> {
    private final T body;
    private final String destination;
    private final String propertyBodyName;

    public Message(T body, String destination) {
        this.body = body;
        this.destination = destination;
        this.propertyBodyName = "value";
    }
}
