package com.example.stubservice.service;

import com.example.stubservice.dto.UserData;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class StubService {
    private static final Set<String> INCORRECT_EMAILS = new HashSet<>(Arrays.asList("string", "test@test.ru"));

    @RabbitListener(queues = "#{queue.name}")
    public Boolean registryConsumer(UserData userData) {
        sleep();
        userData.setApproval(isApproval(userData));
        return userData.isApproval();
    }

    private boolean isApproval(UserData userData) {
        return !INCORRECT_EMAILS.contains(userData.getEmail());
    }

    @SneakyThrows
    private static void sleep() {
        int secondsSleep = new Random().nextInt(10);
        String message = String.format("Sleep by %s", secondsSleep);
        System.out.println(message);
        Thread.sleep(TimeUnit.SECONDS.toMillis(secondsSleep));
//        Thread.sleep(TimeUnit.SECONDS.toMillis(60));
    }
}
