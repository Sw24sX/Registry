package com.example.registry.service;

import com.example.registry.service.persistance.email.EmailAddress;
import com.example.registry.service.persistance.email.EmailContent;

import java.util.concurrent.TimeoutException;

/**
 * Сервис отправки сообщений
 */
public interface SendMailer {
    /**
     * Отправить сообщение
     * @param toAddress одрес получателя
     * @param messageBody сообщение получателю
     * @throws TimeoutException редко может упасть исключение
     */
    void sendMail (EmailAddress toAddress, EmailContent messageBody) throws TimeoutException;
}
