package com.example.registry.service.impl;

import com.example.registry.dto.UserDataRequest;
import com.example.registry.mapper.UserDataMapper;
import com.example.registry.model.UserData;
import com.example.registry.service.MessagingServiceFacade;
import com.example.registry.service.RegistryService;
import com.example.registry.service.SendMailer;
import com.example.registry.service.UserDataService;
import com.example.registry.service.persistance.dto.EmailAddress;
import com.example.registry.service.persistance.dto.RegistryEmailContent;
import com.example.registry.service.persistance.exception.RegistryException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
public class RegistryServiceImpl implements RegistryService {
    private final MessagingServiceFacade messagingServiceFacade;
    private final UserDataMapper userDataMapper;
    private final SendMailer sendMailer;
    private final UserDataService userDataService;

    public RegistryServiceImpl(MessagingServiceFacade messagingServiceFacade, UserDataMapper userDataMapper,
                               SendMailer sendMailer, UserDataService userDataService) {
        this.messagingServiceFacade = messagingServiceFacade;
        this.userDataMapper = userDataMapper;
        this.sendMailer = sendMailer;
        this.userDataService = userDataService;
    }

    @Override
    public UserDataRequest registry(UserDataRequest userDataRequest) {
        UserData userData = userDataService.create(userDataMapper.toUserData(userDataRequest));
        Boolean isApproval = messagingServiceFacade.requestToStub(userDataMapper.toMessage(userData));
        userData.setApproval(isApproval);
        userDataService.flush();
        sendMail(userData);
        return userDataMapper.toRequest(userData);
    }

    private void sendMail(UserData userData) {
        try {
            EmailAddress emailAddress = new EmailAddress(userData.getEmail());
            if (userData.isApproval()) {
                sendMailer.sendMail(emailAddress, new RegistryEmailContent("Success"));
            } else {
                sendMailer.sendMail(emailAddress, new RegistryEmailContent("Wrong"));
            }
        } catch (TimeoutException e) {
            throw new RegistryException(e.getMessage(), e.getCause());
        }
    }
}
