package com.example.registry.service.impl;

import com.example.registry.dto.UserDataRequest;
import com.example.registry.mapper.UserDataMapper;
import com.example.registry.model.UserData;
import com.example.registry.service.RegistryService;
import com.example.registry.service.SendMailer;
import com.example.registry.service.UserDataService;
import com.example.registry.service.persistance.dto.EmailAddress;
import com.example.registry.service.persistance.dto.RegistryEmailContent;
import com.example.registry.service.persistance.exception.RegistryException;
import com.example.registry.service.persistance.message.dto.Message;
import com.example.registry.service.persistance.message.impl.BooleanResponseMessaging;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.util.concurrent.TimeoutException;

@Service
public class RegistryServiceImpl implements RegistryService {
    private final BooleanResponseMessaging booleanResponseMessaging;
    private final UserDataMapper userDataMapper;
    private final SendMailer sendMailer;
    private final UserDataService userDataService;

    public RegistryServiceImpl(BooleanResponseMessaging booleanResponseMessaging, UserDataMapper userDataMapper,
                               SendMailer sendMailer, UserDataService userDataService) {
        this.booleanResponseMessaging = booleanResponseMessaging;
        this.userDataMapper = userDataMapper;
        this.sendMailer = sendMailer;
        this.userDataService = userDataService;
    }

    @Override
    public UserDataRequest registry(UserDataRequest userDataRequest) {
        UserData userData = userDataService.create(userDataMapper.toUserData(userDataRequest));
        try {
            Message<UserData> message = new Message<>(userData, "registry", "userData");
            Message<Boolean> responseMessage = booleanResponseMessaging.doRequest(message);
            userData.setApproval(responseMessage.getBody());
            userDataService.flush();
        } catch (TimeoutException | JMSException e) {
            // TODO: 09.05.2022 add constructor with exception
            throw new RegistryException(e.getMessage(), e.getCause());
        }

        userData.setApproval(userData.isApproval());
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
