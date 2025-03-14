package com.example.subscriptionservice.model;

import com.example.subscriptionservice.model.dto.SubscriptionDto;
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

    SubscriptionDto toDto(Subscription entity);
}
