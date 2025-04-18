package com.example;

import com.example.notificationservice.model.CurrencyEvent;
import com.example.notificationservice.service.EmailSenderService;
import com.example.notificationservice.service.NotificationTemplate;
import com.example.subscriptionapi.dto.SubscriptionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;


class NotificationTemplateTest {

    private EmailSenderService emailSenderService;
    private NotificationTemplate template;

    @BeforeEach
    void setUp() {
        emailSenderService = mock(EmailSenderService.class);
        template = new NotificationTemplate(emailSenderService) {
            @Override
            protected void validateSubscriptionCase(SubscriptionDto subscription, CurrencyEvent event) {
            }

            @Override
            protected String prepareEmailBody(SubscriptionDto subscription, CurrencyEvent event) {
                return "Test body";
            }

            @Override
            protected String prepareEmailSubject(SubscriptionDto subscription, CurrencyEvent event) {
                return "Test subject";
            }
        };
    }

    @Test
    void shouldNotSendEmailIfSubscriptionIsInactive() {
        // given: there is an inactive subscription
        SubscriptionDto subscription = new SubscriptionDto();
        subscription.setIsActive(false);

        CurrencyEvent event = new CurrencyEvent();

        // when: notification is proceeded
        template.process(subscription, event);

        // then: notification is not sent
        verifyNoInteractions(emailSenderService);
    }

    @Test
    void shouldSendEmailIfSubscriptionIsActive() {
        // given: there is an active subscription
        SubscriptionDto subscription = new SubscriptionDto();
        subscription.setIsActive(true);
        subscription.setEmail("test@example.com");

        CurrencyEvent event = new CurrencyEvent();

        // when: notification is proceeded
        template.process(subscription, event);

        // then: notification is sent
        verify(emailSenderService).sendEmail("test@example.com", "Test subject", "Test body");
    }
}

