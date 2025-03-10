package com.example.notificationservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "event-topic", groupId = "new-currency-rates")
    public void listenToEvent(String message) {
        System.out.println("Received message: " + message);
    }
}
