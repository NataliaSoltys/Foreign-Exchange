package com.example;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.notificationservice.model.CurrencyRateDto;
import com.example.notificationservice.service.KafkaConsumer;
import com.example.notificationservice.service.NewCurrencyRateNotification;
import com.example.notificationservice.service.NotificationTemplate;
import com.example.notificationservice.service.NotificationWebService;
import com.example.subscriptionapi.dto.SubscriptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private NotificationWebService notificationWebService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Map<String, NotificationTemplate> templateMap;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @Mock
    private NewCurrencyRateNotification templateForNewCurrencyRate;

    @BeforeEach
    void setUp() {
        kafkaConsumer = new KafkaConsumer(notificationWebService, Map.of(
                "NEW_CURRENCY_RATE", templateForNewCurrencyRate
        ), objectMapper);
    }

    @Test
    void shouldUseCorrectNotificationTemplateBasedOnType() throws Exception {
        // given: there are currency data that are needed to send email
        String message = "some-message";
        CurrencyEvent currencyEvent = CurrencyEvent.builder()
                .rates(List.of(new CurrencyRateDto()))
                .build();

        // and: there are subscription data
        SubscriptionDto subscription = new SubscriptionDto();
        subscription.setIsActive(true);
        subscription.setEmail("test@example.com");
        subscription.setUserFirstName("Natalia");
        subscription.setCurrencyCode("EUR");

        Map<String, List<SubscriptionDto>> subscriptions = Map.of(
                "NEW_CURRENCY_RATE", List.of(subscription)
        );

        when(objectMapper.readValue(message, CurrencyEvent.class)).thenReturn(currencyEvent);
        when(notificationWebService.fetchSubscriptions()).thenReturn(subscriptions);

        // when: kafka even occurs
        kafkaConsumer.listenToEvent(message);

        // then:
        verify(templateForNewCurrencyRate).process(subscription, currencyEvent);
    }
}
