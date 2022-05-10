package com.example.registry.service;

import com.example.registry.service.persistance.model.UserData;

/**
 * Сервис работы с пользовательскими данными
 */
public interface UserDataService {
    /**
     * Создать пользователя в бд
     * @param userData данные пользователя
     * @return данные из бд
     */
    UserData create(UserData userData);

    /**
     * Сохранить текущие изменения в бд
     */
    void flush();
}
