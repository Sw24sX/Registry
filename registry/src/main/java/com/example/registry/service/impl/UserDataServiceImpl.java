package com.example.registry.service.impl;

import com.example.registry.model.UserData;
import com.example.registry.service.UserDataService;
import org.springframework.stereotype.Service;

@Service
public class UserDataServiceImpl implements UserDataService {
    private final MessagingServiceFacade messagingService;
    private final UserDataMapper userDataMapper;
    private final UserDataRepository userDataRepository;

    public UserDataServiceImpl(MessagingServiceFacade messagingService, UserDataMapper userDataMapper, UserDataRepository userDataRepository) {
        this.messagingService = messagingService;
        this.userDataMapper = userDataMapper;
        this.userDataRepository = userDataRepository;
    }

    //TODO transactional
    @Override
    public UserData create(UserData userData) {
        return null;
    }
}
