package com.ratesservice.service.kafka;

import com.ratesservice.model.dto.CurrencyResponseDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaPublisher {

    private final KafkaTemplate<String, CurrencyResponseDto> kafkaTemplate;
    private static final String TOPIC = "new-currency";
    private static Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);

    public void sendEvent(CurrencyResponseDto currencyResponseDto) {
        logger.info("Sending currencyResponseDto: {}", currencyResponseDto);
        kafkaTemplate.send(TOPIC, currencyResponseDto);
        logger.info("Message sent: {}", currencyResponseDto);
    }
}