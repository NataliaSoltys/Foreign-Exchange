package com.example.subscriptionservice.model;

import com.example.subscriptionapi.dto.SubscriptionDto;
import com.example.subscriptionservice.model.entities.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "email", target = "appUser.email")
    @Mapping(source = "userId", target = "appUser.id")
    @Mapping(source = "userFirstName", target = "appUser.firstName")
    @Mapping(source = "userLastName", target = "appUser.lastName")
    @Mapping(source = "phoneNumber", target = "appUser.phoneNumber")
    @Mapping(source = "address", target = "appUser.address")
    @Mapping(source = "country", target = "appUser.country")
    @Mapping(source = "currencyCode", target = "currencyCode")
    @Mapping(source = "subscriptionType", target = "subscriptionType")
    @Mapping(source = "sellPriceBoundaryValue", target = "sellPriceBoundaryValue")
    @Mapping(source = "buyPriceBoundaryValue", target = "buyPriceBoundaryValue")
    @Mapping(source = "isActive", target = "isActive")
    Subscription toEntity(SubscriptionDto dto);

    @Mapping(source = "appUser.address", target = "address")
    @Mapping(source = "appUser.id", target = "userId")
    @Mapping(source = "appUser.email", target = "email")
    @Mapping(source = "appUser.firstName", target = "userFirstName")
    @Mapping(source = "appUser.lastName", target = "userLastName")
    @Mapping(source = "appUser.phoneNumber", target = "phoneNumber")
    @Mapping(source = "appUser.country", target = "country")
    SubscriptionDto toDto(Subscription entity);
}

