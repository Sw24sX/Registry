package com.example.stubservice.service;

import com.example.stubservice.dto.UserData;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StubService {

    @RabbitListener(queues = "#{queue.name}")
    public UserData registryConsumer(UserData userData) {
        userData.setApproved(true);
        return userData;
    }
}
