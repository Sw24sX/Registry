package com.example.registry.service.persistance.validator;

import com.example.registry.model.UserData;

import java.util.regex.Pattern;

public class UserDataValidator {
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final String LOGIN_REGEX = "[A-Za-z0-9_-]+";

    private UserDataValidator() {
    }

    public static boolean isEmailValid(String email) {
        return patternMatches(email, EMAIL_REGEX);
    }

    public static boolean isLoginValid(String login) {
        return patternMatches(login, LOGIN_REGEX);
    }

    public static boolean isNameValid(String name) {
        return patternMatches(name, LOGIN_REGEX);
    }

    public static boolean isUserDataValid(UserData userData) {
        return isEmailValid(userData.getEmail()) && isNameValid(userData.getName()) && isLoginValid(userData.getLogin());
    }

    private static boolean patternMatches(String value, String pattern) {
        return Pattern.compile(value)
                .matcher(pattern)
                .matches();
    }
}
