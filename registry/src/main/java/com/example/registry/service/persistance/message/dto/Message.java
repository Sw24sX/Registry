package com.example.registry.service.persistance.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message<T> {
    private T body;
    private String queueName;
    private String propertyBodyName;
}
