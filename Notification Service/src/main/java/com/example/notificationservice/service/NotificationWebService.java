package com.example.notificationservice.service;

import com.example.subscriptionapi.dto.SubscriptionDto;
import com.example.subscriptionapi.dto.SubscriptionType;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.notificationservice.web.url.HttpUrls.GET_ALL_SUBSCRIPTIONS_GROUPED_BY_TYPE;
import static com.example.notificationservice.web.url.HttpUrls.GET_SUBSCRIPTIONS_BY_TYPE;


@Service
@AllArgsConstructor
public class NotificationWebService {

    private final WebClient.Builder webClientBuilder;
    private static Logger logger = LoggerFactory.getLogger(NotificationWebService.class);

    public Map<String, List<SubscriptionDto>> fetchSubscriptions() {
        WebClient webClient = webClientBuilder.baseUrl(GET_ALL_SUBSCRIPTIONS_GROUPED_BY_TYPE).build();
        Map<String, List<SubscriptionDto>> subscriptionsDtos = getSubscriptionsDtoByType(webClient);
        logger.info("Fetched {} subscriptions: {}", subscriptionsDtos.size(), subscriptionsDtos);
        return subscriptionsDtos;
    }

    private static Map<String, List<SubscriptionDto>> getSubscriptionsDtoByType(WebClient webClient) {
        return webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, List<SubscriptionDto>>>() {})
                .block();
    }
}

