package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.subscriptionapi.dto.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        templateMap.keySet().forEach(
                notificationTypeClass -> {
                    log.info("Processing notifications for type: {}", notificationTypeClass);
                    subscriptionsByType.get(notificationTypeClass).forEach(
                            subscription -> {
                                log.info("Starting sending notification process to userId{} for type {}", subscription.getUserId(), notificationTypeClass);
                                templateMap.get(notificationTypeClass).process(subscription, event);
                            }
                    );
                    log.info("Finished processing notifications for type: {}", notificationTypeClass); // todo: make 2 iteration only
                }
        );

        log.info("Finished sending emails for event: {}", event);
    }
}