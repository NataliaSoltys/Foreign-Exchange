package com.example.notificationservice.service;

import com.example.subscriptionapi.dto.SubscriptionDto;
import com.example.subscriptionapi.dto.SubscriptionType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class KafkaConsumer {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private NotificationWebService notificationWebService;

    @KafkaListener(topics = "new-currency", groupId = "new-currency-rates")
    public void listenToEvent(String message) {
        logger.info("Received message: {}", message);
        Map<String, List<SubscriptionDto>> subscriptionDtos = notificationWebService.fetchSubscriptions();
        logger.info("After receiving the message, fetched subscriptions: {}", subscriptionDtos);
    }
}
