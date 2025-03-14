package com.example.subscriptionservice.model;

import com.example.subscriptionservice.model.dto.UserDto;
import com.example.subscriptionservice.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "email", target = "email")
    @Mapping(source = "userFirstName", target = "firstName")
    @Mapping(source = "userLastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "country", target = "country")
    @Mapping(source = "address", target = "address")
    User toEntity(UserDto dto);

}
