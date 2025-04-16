package com.ratesservice.junitTests;

import com.ratesservice.model.dto.CurrencyRateDto;
import com.ratesservice.model.dto.CurrencyResponseDto;
import com.ratesservice.model.entites.CurrencyRate;
import com.ratesservice.service.CurrencyApi;
import com.ratesservice.service.CurrencyService;
import com.ratesservice.service.kafka.KafkaPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;


class CurrencyServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private CurrencyApi currencyApi;

    @Mock
    private KafkaPublisher kafkaPublisher;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFetchFromApiAndPublishToKafkaAndSaveToDb() {
        // given: there are data ready to be performed
        String date = "2025-04-14";
        CurrencyRateDto dto = new CurrencyRateDto();
        dto.setCode("USD");
        dto.setCurrencyName("dolar amerykański");
        dto.setBuyPrice(3.75f);
        dto.setSellPrice(3.85f);

        CurrencyResponseDto responseDto = CurrencyResponseDto.builder()
                .effectiveDate(date)
                .rates(List.of(dto))
                .build();

        List<CurrencyRate> savedRates = List.of(new CurrencyRate());

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class)))
                .thenReturn(Mono.just(List.of(responseDto)));

        when(currencyApi.addCurrencyRates(responseDto)).thenReturn(savedRates);

        // when: currency rates are being fetched
        currencyService.fetchCurrencyRates();

        // then: proper methods are being called
        verify(currencyApi).addCurrencyRates(responseDto);
        verify(kafkaPublisher).sendEvent(any(CurrencyResponseDto.class));
    }
}