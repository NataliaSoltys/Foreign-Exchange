package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyResponseDto;
import com.example.subscriptionapi.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class NotificationTemplate {

    private final EmailSenderService emailSenderService;

    public final void process(SubscriptionDto subscription, CurrencyResponseDto event) {
        if (!subscription.getIsActive()) return;
        String emailContent = prepareEmailBody(subscription, event);
        String subject = prepareEmailSubject(subscription, event);
        emailSenderService.sendEmail(subscription.getEmail(), subject, emailContent);
    }

    protected abstract String prepareEmailBody(SubscriptionDto subscription, CurrencyResponseDto event);

    protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyResponseDto event) {
        return "Powiadomienie walutowe";
    }

    public abstract String getType();

}