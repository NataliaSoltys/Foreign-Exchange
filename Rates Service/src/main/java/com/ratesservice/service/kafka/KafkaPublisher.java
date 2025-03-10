package com.ratesservice.service.kafka;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "event-topic";

    public void sendEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}