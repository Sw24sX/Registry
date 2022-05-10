package com.example.registry.service.persistance.email;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class EmailContent {
    private String template;
    private Map<String, Object> variables;
}
