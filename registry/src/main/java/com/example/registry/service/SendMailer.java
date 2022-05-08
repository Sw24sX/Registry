package com.example.registry.service;

import com.example.registry.service.persistance.dto.EmailAddress;
import com.example.registry.service.persistance.dto.EmailContent;

import java.util.concurrent.TimeoutException;

public interface SendMailer {
    void sendMail (EmailAddress toAddress, EmailContent messageBody) throws TimeoutException;
}
