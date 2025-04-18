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

import static com.ratesservice.web.HttpUrls.GET_ALL_EXCHANGE_RATES;


@Service
@AllArgsConstructor
public class CurrencyService {

    private final WebClient.Builder webClientBuilder;
    private final CurrencyApi currencyApi;
    private static Logger logger = LoggerFactory.getLogger(CurrencyService.class);
    private final KafkaPublisher kafkaPublisher;

    //    @Scheduled(cron = "0 0 0 * * ?")  // everyday
    @Scheduled(cron = "0 */2 * * * *") // for testing only
    public void fetchCurrencyRates() {
        WebClient webClient = webClientBuilder.baseUrl(GET_ALL_EXCHANGE_RATES).build();
        CurrencyResponseDto responseDto = getCurrencyResponseDtoMono(webClient);
        logger.info("Currency rates deserialized: {}", responseDto);
        kafkaPublisher.sendEvent(CurrencyResponseDto.builder().effectiveDate(responseDto.getEffectiveDate()).rates(responseDto.getRates()).build());
        List<CurrencyRate> currencyRates = currencyApi.addCurrencyRates(responseDto);
        logger.info("Currency rates saved to db: {}", currencyRates);
    }

    private static CurrencyResponseDto getCurrencyResponseDtoMono(WebClient webClient) {
        return webClient.get()
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CurrencyResponseDto>>() {
                })
                .blockOptional()
                .map(list -> list.isEmpty() ? null : list.getFirst())
                .orElse(null);
    }
}

