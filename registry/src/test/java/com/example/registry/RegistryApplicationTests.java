package com.example.registry;

import com.example.registry.common.UserDataSet;
import com.example.registry.model.UserData;
import com.example.registry.repository.UserDataRepository;
import com.example.registry.service.persistance.message.MessagingService;
import com.example.registry.service.UserDataService;
import com.example.registry.service.persistance.exception.EntityAlreadyExist;
import com.example.registry.service.persistance.exception.InvalidEntityException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class RegistryApplicationTests {
    @Autowired
    private UserDataService userDataService;

    @MockBean
    private UserDataRepository userDataRepository;

    @MockBean
    private MessagingService messagingService;

    @Test
    public void createUser_validUser() {
        UserData userData = UserDataSet.createValid();
        Mockito.when(userDataRepository.existsByLoginOrEmail(any(), any())).thenReturn(false);
        Mockito.when(userDataRepository.save(userData)).thenReturn(userData);

        UserData data = userDataService.create(userData);
        assertThat(data)
                .usingRecursiveComparison()
                .isEqualTo(userData);
    }

    @Test
    public void createUser_validUserExists() {
        UserData userData = UserDataSet.createValid();
        Mockito.when(userDataRepository.existsByLoginOrEmail(any(), any())).thenReturn(true);
        Mockito.when(userDataRepository.save(userData)).thenReturn(userData);
        assertThrows(EntityAlreadyExist.class, () -> userDataService.create(userData));
    }

    @Test
    public void createUser_invalidUser() {
        UserData userData = UserDataSet.createInvalid();
        assertThrows(InvalidEntityException.class, () -> userDataService.create(userData));
    }
}
