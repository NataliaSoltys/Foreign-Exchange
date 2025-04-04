package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.subscriptionapi.dto.SubscriptionDto;
import org.springframework.stereotype.Component;

@Component("NEW_CURRENCY_RATE")
public class NewCurrencyRateNotification extends NotificationTemplate {

    public NewCurrencyRateNotification(EmailSenderService emailSenderService) {
        super(emailSenderService);
    }

    @Override
    protected String prepareEmailBody(SubscriptionDto subscription, CurrencyEvent event) {
        return String.format("Cześć %s! Nowy kurs waluty %s: .2f",
                subscription.getUserFirstName(),
                subscription.getCurrencyCode());
    }

    @Override
    protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyEvent event) {
        return "New currency rate available!";
    }

}