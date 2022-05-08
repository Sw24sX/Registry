package com.example.registry.service.persistance.message;

import com.example.registry.service.persistance.Routing;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message<T> {
    private T body;
    private Routing routing;
}
