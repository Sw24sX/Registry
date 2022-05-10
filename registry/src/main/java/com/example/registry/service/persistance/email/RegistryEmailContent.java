package com.example.registry.service.persistance.email;

import java.util.Collections;

public class RegistryEmailContent extends EmailContent{
    public RegistryEmailContent(String message) {
        super("registry", Collections.singletonMap("message", message));
    }
}
