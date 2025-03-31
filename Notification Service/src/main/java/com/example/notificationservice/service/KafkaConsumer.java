package com.example.notificationservice.service;

import com.example.notificationservice.model.CurrencyResponseDto;
import com.example.subscriptionapi.dto.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class KafkaConsumer {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final NotificationWebService notificationWebService;
    private final List<NotificationTemplate> notificationTemplates;
    private final Map<String, NotificationTemplate> templateMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "new-currency", groupId = "new-currency-rates")
    public void listenToEvent(String message) {
        CurrencyResponseDto event;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            event = objectMapper.readValue(message, CurrencyResponseDto.class);
            logger.info("Received event: {}", event);
        } catch (Exception e) {
            logger.error("❌ Błąd przy deserializacji wiadomości do CurrencyResponseDto", e);
            return;
        }
        Map<String, List<SubscriptionDto>> subscriptionDtos = notificationWebService.fetchSubscriptions();

        if (templateMap.isEmpty()) {
            for (NotificationTemplate template : notificationTemplates) {
                templateMap.put(template.getType(), template);
            }
        }

        subscriptionDtos.forEach((type, subscriptions) -> {
            NotificationTemplate notification = templateMap.get(type);
            if (notification != null) {
                subscriptions.forEach(sub -> notification.process(sub, event));
            } else {
                logger.warn("Brak zarejestrowanej notyfikacji dla typu: {}", type);
            }
        });

        logger.info("Notifications sent for event: {}", event);
    }
}
