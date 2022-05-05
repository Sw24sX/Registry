package com.example.registry.service;

import com.example.registry.service.persistance.EmailAddress;
import com.example.registry.service.persistance.EmailContent;

import java.util.concurrent.TimeoutException;

public interface SendMailer {
    void sendMail (EmailAddress toAddress, EmailContent messageBody) throws TimeoutException;
}
