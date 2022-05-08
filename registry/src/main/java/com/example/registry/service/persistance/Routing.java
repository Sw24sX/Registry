package com.example.registry.service.persistance;

public enum Routing {
    REGISTRY("registry");

    private final String value;

    Routing(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
