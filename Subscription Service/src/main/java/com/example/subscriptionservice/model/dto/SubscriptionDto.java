package com.example.subscriptionservice.model.dto;

import com.example.subscriptionservice.model.enums.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubscriptionDto {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("subscriptionType")
    private SubscriptionType subscriptionType;
    @JsonProperty("userFirstName")
    private String userFirstName;
    @JsonProperty("userLastName")
    private String userLastName;
    @JsonProperty("sellPriceBoundaryValue")
    private Float sellPriceBoundaryValue;
    @JsonProperty("buyPriceBoundaryValue")
    private Float buyPriceBoundaryValue;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("address")
    private String address;
    @JsonProperty("country")
    private String country;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
}
