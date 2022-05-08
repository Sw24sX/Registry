package com.example.registry.mapper;

import com.example.registry.dto.UserDataRequest;
import com.example.registry.model.UserData;
import com.example.registry.service.persistance.message.UserDataMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDataMapper {
    @Mapping(target = "approval", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserData toUserData(UserDataRequest request);

    UserData toUserData(UserDataMessage message);

    UserDataMessage toMessage(UserData userData);

    UserDataRequest toRequest(UserData userData);
}
