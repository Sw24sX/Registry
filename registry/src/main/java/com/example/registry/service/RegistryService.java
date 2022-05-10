package com.example.registry.service;

import com.example.registry.controller.dto.UserDataRequest;

/**
 * Сервис регистрации пользователя
 */
public interface RegistryService {
    /**
     * Регистрация пользователя
     * @param userDataRequest Данные пользователя
     * @return сохраненные данные пользователя
     */
    UserDataRequest registry(UserDataRequest userDataRequest);
}
