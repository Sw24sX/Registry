package com.example.registry;

import com.example.registry.common.UserDataSet;
import com.example.registry.service.persistance.model.UserData;
import com.example.registry.service.persistance.validator.UserDataValidator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ValidatorTests {
    @Test
    public void testValidUser() {
        UserData userData = UserDataSet.createValid();
        assertTrue(UserDataValidator.isEmailValid(userData.getEmail()));
        assertTrue(UserDataValidator.isLoginValid(userData.getLogin()));
        assertTrue(UserDataValidator.isNameValid(userData.getName()));
        assertTrue(UserDataValidator.isUserDataValid(userData));
    }

    @Test
    public void testInvalidUser() {
        UserData userData = UserDataSet.createInvalid();
        assertFalse(UserDataValidator.isEmailValid(userData.getEmail()));
        assertFalse(UserDataValidator.isLoginValid(userData.getLogin()));
        assertFalse(UserDataValidator.isNameValid(userData.getName()));
        assertFalse(UserDataValidator.isUserDataValid(userData));
    }
}
