package com.example.registry.service.impl;

import com.example.registry.service.SendMailer;
import com.example.registry.service.persistance.dto.EmailAddress;
import com.example.registry.service.persistance.dto.EmailContent;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SendMailerStub implements SendMailer {

    @Override
    public void sendMail(EmailAddress toAddress, EmailContent messageBody) throws TimeoutException {
        if(shouldThrowTimeout()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if(shouldSleep()) {
            sleep();
        }

        // ok.
//        log.info("Message sent to {}, body {}.", toAddress, messageBody);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}
