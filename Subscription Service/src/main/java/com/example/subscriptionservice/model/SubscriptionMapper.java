package com.example.subscriptionservice.model;

import com.example.subscriptionapi.dto.SubscriptionDto;
import com.example.subscriptionservice.model.entities.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "email", target = "user.email")
    @Mapping(source = "currencyCode", target = "currencyCode")
    @Mapping(source = "subscriptionType", target = "subscriptionType")
    @Mapping(source = "userFirstName", target = "user.firstName")
    @Mapping(source = "userLastName", target = "user.lastName")
    @Mapping(source = "buyPriceBoundaryValue", target = "buyPriceBoundaryValue")
    @Mapping(source = "sellPriceBoundaryValue", target = "sellPriceBoundaryValue")
    @Mapping(source = "phoneNumber", target = "user.phoneNumber")
    @Mapping(source = "country", target = "user.country")
    @Mapping(source = "address", target = "user.address")
    Subscription toEntity(SubscriptionDto dto);

    @Mapping(source = "user.address", target = "address")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "user.country", target = "country")
    SubscriptionDto toDto(Subscription entity);
}
