package com.example.registry.mapper;

import com.example.registry.controller.dto.UserDataRequest;
import com.example.registry.service.persistance.model.UserData;
import com.example.registry.message.dto.UserDataMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDataMapper {
    @Mapping(target = "approval", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserData toUserData(UserDataRequest request);

    UserDataMessage toMessage(UserData userData);

    UserDataRequest toRequest(UserData userData);
}
