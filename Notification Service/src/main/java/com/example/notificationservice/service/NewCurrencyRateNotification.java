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
    protected void validateSubscriptionCase(SubscriptionDto subscription, CurrencyEvent event) {

    }

    @Override
    protected String prepareEmailBody(SubscriptionDto subscription, CurrencyEvent event) {
        return String.format("Hi %s! The new rate is available.",
                subscription.getUserFirstName());
    }

    @Override
    protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyEvent event) {
        return "New currency rate available!";
    }

}