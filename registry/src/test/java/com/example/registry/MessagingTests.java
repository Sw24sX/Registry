package com.example.registry;

import com.example.registry.common.UserDataSet;
import com.example.registry.controller.dto.UserDataRequest;
import com.example.registry.mapper.UserDataMapper;
import com.example.registry.message.dto.Message;
import com.example.registry.message.messaging.BooleanResponseMessaging;
import com.example.registry.service.persistance.model.UserData;
import com.example.registry.service.RegistryService;
import com.example.registry.service.SendMailer;
import com.example.registry.service.UserDataService;
import com.example.registry.service.persistance.exception.RegistryException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.jms.JMSException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class MessagingTests {
    @MockBean
    private UserDataService userDataService;

    @MockBean
    private SendMailer sendMailer;

    @MockBean
    private BooleanResponseMessaging messagingService;

    @Autowired
    private RegistryService registryService;

    @Autowired
    private UserDataMapper userDataMapper;

    @Test
    public void verifyRegistry_Success() throws TimeoutException, JMSException {
        UserData userData = UserDataSet.createValid();
        Mockito.when(userDataService.create(any())).thenReturn(userData);
        Mockito.when(messagingService.doRequest(new Message<UserData>(any(), "stub-service"))).
                thenReturn(new Message<>(true, null, null));
        UserDataRequest actual = registryService.registry(userDataMapper.toRequest(userData));
        assertTrue(actual.isApproval());
    }

    @Test
    public void verifyRegistry_Wrong() throws TimeoutException, JMSException {
        UserData userData = UserDataSet.createValid();
        Mockito.when(userDataService.create(any())).thenReturn(userData);
        Mockito.when(messagingService.doRequest(new Message<UserData>(any(), "stub-service")))
                .thenReturn(new Message<>(false, null, null));
        UserDataRequest actual = registryService.registry(userDataMapper.toRequest(userData));
        assertFalse(actual.isApproval());
    }

    @Test
    public void verifyRegistry_Timeout() throws TimeoutException, JMSException {
        UserData userData = UserDataSet.createValid();
        Mockito.when(userDataService.create(any())).thenReturn(userData);
        Mockito.when(messagingService.doRequest(new Message<UserData>(any(), "stub-service")))
                .thenThrow(new TimeoutException());
        assertThrows(RegistryException.class, () -> registryService.registry(userDataMapper.toRequest(userData)));
    }
}
