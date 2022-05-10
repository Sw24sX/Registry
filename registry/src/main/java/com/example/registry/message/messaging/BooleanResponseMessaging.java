package com.example.registry.message.messaging;

import com.example.registry.service.persistance.repository.MessageStateRepository;
import com.example.registry.service.persistance.exception.RegistryException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Component
@Qualifier("booleanResponse")
public class BooleanResponseMessaging extends CommonMessagingService {
    private final ObjectMapper objectMapper;

    public BooleanResponseMessaging(JmsTemplate jmsTemplate, ConnectionFactory connectionFactory,
                                    MessageStateRepository messageStateRepository, ObjectMapper objectMapper) throws JMSException {
        super(jmsTemplate, connectionFactory, messageStateRepository);
        this.objectMapper = objectMapper;
    }

    @Override
    protected <T> String convertRequest(T requestBody) {
        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RegistryException(e.getMessage(), e.getCause());
        }
    }

    @Override
    protected <T> T convertResponse(String message) {
        try {
            return (T) objectMapper.readValue(message, Boolean.class);
        } catch (JsonProcessingException e) {
            throw new RegistryException(e.getMessage(), e.getCause());
        }
    }
}
