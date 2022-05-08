package com.example.registry.service.impl;

import com.example.registry.model.UserData;
import com.example.registry.repository.UserDataRepository;
import com.example.registry.service.UserDataService;
import com.example.registry.service.persistance.exception.EntityAlreadyExist;
import com.example.registry.service.persistance.exception.InvalidEntityException;
import com.example.registry.service.persistance.validator.UserDataValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDataServiceImpl implements UserDataService {
    private final UserDataRepository userDataRepository;

    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserData create(UserData userData) {
        validate(userData);
        return userDataRepository.save(userData);
    }

    @Override
    public void flush() {
        userDataRepository.flush();
    }

    private void validate(UserData userData) {
        if (!UserDataValidator.isUserDataValid(userData)) {
            throw new InvalidEntityException();
        }

        if (userDataRepository.existsByLoginOrEmail(userData.getLogin(), userData.getEmail())) {
            throw new EntityAlreadyExist();
        }
    }
}
