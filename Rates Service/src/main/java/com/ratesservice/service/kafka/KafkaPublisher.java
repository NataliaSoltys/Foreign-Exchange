package com.ratesservice.service.kafka;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "new-currency";
    private static Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);

    public void sendEvent(String message) {
        logger.info("Sending message: {}", message);
        kafkaTemplate.send(TOPIC, message);
        logger.info("Message sent: {}", message);
    }
}