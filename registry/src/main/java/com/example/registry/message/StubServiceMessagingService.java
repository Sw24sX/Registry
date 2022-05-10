package com.example.registry.message;

import com.example.registry.message.dto.UserDataMessage;

/**
 * Сервис для отправки сообщений на Stub-service
 */
public interface StubServiceMessagingService {
    /**
     * Отправка данных о пользователе для подтверждения регистрации сторонним сервисом
     * @param userData данные о пользователе
     * @return подтверждение регистрации сторонним сервисом
     */
    Boolean registryUser(UserDataMessage userData);
}
