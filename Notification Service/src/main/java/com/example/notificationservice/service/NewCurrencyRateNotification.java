package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyResponseDto;
import com.example.subscriptionapi.dto.SubscriptionDto;
import org.springframework.stereotype.Component;

@Component
public class NewCurrencyRateNotification extends NotificationTemplate {

    public NewCurrencyRateNotification(EmailSenderService emailSenderService) {
        super(emailSenderService);
    }

    @Override
    protected String prepareEmailBody(SubscriptionDto subscription, CurrencyResponseDto event) {
        return String.format("Cześć %s! Nowy kurs waluty %s: .2f",
                subscription.getUserFirstName(),
                subscription.getCurrencyCode());
    }

    @Override
    protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyResponseDto event) {
        return "Nowy kurs walutowy dostępny!";
    }

    @Override
    public String getType() {
        return "NEW_CURRENCY_RATE";
    }
}