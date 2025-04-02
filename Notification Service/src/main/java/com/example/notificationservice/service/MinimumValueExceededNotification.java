package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.subscriptionapi.dto.SubscriptionDto;
import org.springframework.stereotype.Component;

@Component("MINIMUM_VALUE_EXCEEDED")
public class MinimumValueExceededNotification extends NotificationTemplate {

    public MinimumValueExceededNotification(EmailSenderService emailSenderService) {
        super(emailSenderService);
    }

    @Override
    protected String prepareEmailBody(SubscriptionDto subscription, CurrencyEvent event) {
        return String.format("hI %s! The %s rate has exceeded your limit %.2f",
                subscription.getUserFirstName(),
                subscription.getCurrencyCode(),
                subscription.getBuyPriceBoundaryValue());
    }

    @Override
    protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyEvent event) {
        return "Maximum value exceeded!";
    }
}