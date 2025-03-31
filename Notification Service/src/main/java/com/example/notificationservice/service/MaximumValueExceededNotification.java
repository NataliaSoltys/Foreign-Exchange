package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyResponseDto;
import com.example.subscriptionapi.dto.SubscriptionDto;
import org.springframework.stereotype.Component;

@Component
public class MaximumValueExceededNotification extends NotificationTemplate {

    public MaximumValueExceededNotification(EmailSenderService emailSenderService) {
        super(emailSenderService);
    }

    @Override
    protected String prepareEmailBody(SubscriptionDto subscription, CurrencyResponseDto event) {
        return String.format("Hej %s! Kurs %s przekroczył Twój limit %.2f",
                subscription.getUserFirstName(),
                subscription.getCurrencyCode(),
                subscription.getBuyPriceBoundaryValue());
    }

    @Override
    protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyResponseDto event) {
        return "Przekroczono Twój ustawiony limit!";
    }

    @Override
    public String getType() {
        return "";
    }

}