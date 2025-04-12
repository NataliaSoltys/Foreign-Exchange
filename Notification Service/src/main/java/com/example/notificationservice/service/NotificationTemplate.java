package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.subscriptionapi.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class NotificationTemplate {

    private final EmailSenderService emailSenderService;

    public final void process(SubscriptionDto subscription, CurrencyEvent event) {
        if (!subscription.getIsActive()) return;
        validateSubscriptionCase(subscription, event);
        String emailContent = prepareEmailBody(subscription, event);
        String subject = prepareEmailSubject(subscription, event);
        emailSenderService.sendEmail(subscription.getEmail(), subject, emailContent);
    }

    protected abstract void validateSubscriptionCase(SubscriptionDto subscription, CurrencyEvent event);

    protected abstract String prepareEmailBody(SubscriptionDto subscription, CurrencyEvent event);

    protected abstract String prepareEmailSubject(SubscriptionDto subscription, CurrencyEvent event);

}