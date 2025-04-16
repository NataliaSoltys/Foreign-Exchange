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
class CurrencyApiIntegrationTest {

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

    private CurrencyRateDto createCurrencyRateDto(String code, String name, float bid, float ask) {
        CurrencyRateDto dto = new CurrencyRateDto();
        dto.setCode(code);
        dto.setCurrencyName(name);
        dto.setBuyPrice(bid);
        dto.setSellPrice(ask);
        return dto;
    }
}