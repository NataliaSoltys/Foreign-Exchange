package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.subscriptionapi.dto.SubscriptionDto;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("MINIMUM_VALUE_EXCEEDED")
public class MinimumValueExceededNotification extends NotificationTemplate {

    public MinimumValueExceededNotification(EmailSenderService emailSenderService) {
        super(emailSenderService);
    }

    @Override
    protected void validateSubscriptionCase(SubscriptionDto subscription, CurrencyEvent event) {
        var currencyRateEvent = event.getRates().stream().filter(rate -> rate.getCode().equals(subscription.getCurrencyCode())).collect(Collectors.toList()).get(0);
        if (currencyRateEvent.getBuyPrice() > subscription.getBuyPriceBoundaryValue()) {
            throw new ValidateException("No mail needed, value not exceeded!");
        }
    }

    @Override
    protected String prepareEmailBody(SubscriptionDto subscription, CurrencyEvent event) {
        return String.format("Hi %s! \n The %s buy rate has exceeded your minimum limit %.2f. \n Now it is: %s",
                subscription.getUserFirstName(),
                subscription.getCurrencyCode(),
                subscription.getBuyPriceBoundaryValue(),
                event.getRates().stream().filter(rate -> rate.getCode().equals(subscription.getCurrencyCode())).collect(Collectors.toList()).get(0).getBuyPrice());
    }

    @Override
    protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyEvent event) {
        return "Minimum value exceeded!";
    }
}