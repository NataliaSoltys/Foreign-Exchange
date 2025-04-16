package com.ratesservice;

import com.ratesservice.model.dto.CurrencyRateDto;
import com.ratesservice.model.dto.CurrencyResponseDto;
import com.ratesservice.model.entites.CurrencyRate;
import com.ratesservice.repository.CurrencyRateRepository;
import com.ratesservice.service.CurrencyApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class CurrencyApiIT {

    @Autowired
    private CurrencyApi currencyApi;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Test
    void shouldAddCurrencyRatesAndPersistThemInDatabase() {
        // given: there is a currency ready to save to database
        String date = "2025-04-14";
        CurrencyResponseDto responseDto = CurrencyResponseDto.builder()
                .effectiveDate(date)
                .rates(List.of(createCurrencyRateDto("USD", "dolar amerykański", 3.75f, 3.85f)))
                .build();

        // when: currency is saved after getting data from external API
        currencyApi.addCurrencyRates(responseDto);


        // then: there is 1 row with corrected data in DB
        List<CurrencyRate> savedRates = currencyRateRepository.findAll();
        assertEquals(1, savedRates.size());

        CurrencyRate saved = savedRates.getFirst();
        assertEquals("USD", saved.getCode());
        assertEquals(3.75f, saved.getBuyPrice());
        assertEquals(3.85f, saved.getSellPrice());
        assertEquals(LocalDate.parse(date), saved.getDate());
    }

    @Test
    void shouldFindCurrencyRateByDate() {
        // given: there are already data saved in DB
        String date = "2025-04-15";
        CurrencyResponseDto responseDto = CurrencyResponseDto.builder()
                .effectiveDate(date)
                .rates(List.of(createCurrencyRateDto("EUR", "euro", 4.20f, 4.30f)))
                .build();

        CurrencyResponseDto responseDto2 = CurrencyResponseDto.builder()
                .effectiveDate("2025-04-16")
                .rates(List.of(createCurrencyRateDto("EUR", "euro", 4.20f, 4.30f)))
                .build();

        currencyApi.addCurrencyRates(responseDto);
        currencyApi.addCurrencyRates(responseDto2);

        // when: user requests to get currencies by date
        List<CurrencyRate> result = currencyApi.findByDate(LocalDate.parse(date));

        // then: currencies by date are being fetched
        assertEquals(1, result.size());
        assertEquals("EUR", result.getFirst().getCode());
    }

    @Test
    void shouldFindCurrencyRateByCode() {
        // given: there are already data saved in DB
        String date = "2025-04-18";
        CurrencyResponseDto responseDto = CurrencyResponseDto.builder()
                .effectiveDate(date)
                .rates(List.of(createCurrencyRateDto("GBP", "funt szterling", 5.10f, 5.20f),
                        createCurrencyRateDto("USD", "dolar amerykanski", 3.80f, 3.90f)))
                .build();
        currencyApi.addCurrencyRates(responseDto);

        // when: user requests to get currencies by date
        List<CurrencyRate> result = currencyApi.findByCode("GBP");

        // then: currencies by code are being fetched
        assertEquals(1, result.size());
        assertEquals("GBP", result.getFirst().getCode());
        assertEquals(LocalDate.parse(date), result.getFirst().getDate());
    }

    private CurrencyRateDto createCurrencyRateDto(String code, String name, float bid, float ask) {
        CurrencyRateDto dto = new CurrencyRateDto();
        dto.setCode(code);
        dto.setCurrencyName(name);
        dto.setBuyPrice(bid);
        dto.setSellPrice(ask);
        return dto;
    }
}