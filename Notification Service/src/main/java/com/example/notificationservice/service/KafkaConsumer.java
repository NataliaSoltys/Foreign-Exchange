package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.subscriptionapi.dto.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaConsumer {

    private final NotificationWebService notificationWebService;
    private final Map<String, NotificationTemplate> templateMap;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "new-currency", groupId = "new-currency-rates")
    public void listenToEvent(String message) {
        CurrencyEvent currencyEventEvent = deserializeMessage(message);
        if (currencyEventEvent == null) return;
        Map<String, List<SubscriptionDto>> groupedSubscriptions = notificationWebService.fetchSubscriptions();
        processSubscriptions(groupedSubscriptions, currencyEventEvent);
    }

    private CurrencyEvent deserializeMessage(String message) {
        try {
            CurrencyEvent dto = objectMapper.readValue(message, CurrencyEvent.class);
            log.info("Received currency event: {}", dto);
            return dto;
        } catch (Exception e) {
            log.error("Failed to deserialize message: {}", message, e);
            return null;
        }
    }

    private void processSubscriptions(Map<String, List<SubscriptionDto>> subscriptionsByType, CurrencyEvent event) {
        subscriptionsByType.forEach((type, subscriptions) -> {
            NotificationTemplate notification = templateMap.get(type);
            log.info("Processing notifications for type: {}", type);
            for (SubscriptionDto subscription : subscriptions) {
                try {
                    log.info("Sending notification for subscription: {}", subscription);
                    notification.process(subscription, event);
                } catch (ValidateException e) {
                    log.info("Skipping notification: {}", e.getMessage());
                }
            }
        });
    }


}