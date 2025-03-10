package com.ratesservice.service;

import com.ratesservice.model.dto.CurrencyResponseDto;
import com.ratesservice.model.entites.CurrencyRate;
import com.ratesservice.service.kafka.KafkaPublisher;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.ratesservice.web.HttpUrls.getAllExchangeRates;


@Service
@AllArgsConstructor
public class CurrencyService {

    private final WebClient.Builder webClientBuilder;
    private final CurrencyApi currencyApi;
    private static Logger logger = LoggerFactory.getLogger(CurrencyService.class);
    private final KafkaPublisher kafkaPublisher;

//    @Scheduled(cron = "0 0 0 * * ?")  // everyday
    @Scheduled(cron = "*/10 * * * * *") // for testing only
    public void fetchCurrencyRates() {
        //kafkaPublisher.sendEvent("TEST SEND");
        WebClient webClient = webClientBuilder.baseUrl(getAllExchangeRates).build();
        CurrencyResponseDto responseDto = getCurrencyResponseDtoMono(webClient);
        logger.info("Currency rates deserialized: {}", responseDto);
        List<CurrencyRate>  currencyRates = currencyApi.addCurrencyRates(responseDto);
//        kafkaPublisher.sendEvent("New currency rates occurred: " + currencyRates);
        logger.info("Currency rates saved to db: {}", currencyRates);
    }

    private static CurrencyResponseDto getCurrencyResponseDtoMono(WebClient webClient) {
        return webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CurrencyResponseDto>>() {})
                .blockOptional()
                .map(list -> list.isEmpty() ? null : list.getFirst())
                .orElse(null);
    }
}

