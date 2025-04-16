package com.ratesservice.service;

import com.ratesservice.model.dto.CurrencyResponseDto;
import com.ratesservice.model.entites.CurrencyRate;
import com.ratesservice.model.entites.CurrencyRateMapper;
import com.ratesservice.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyApi {

    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRateValidator currencyRateValidator;

    public List<CurrencyRate> addCurrencyRates(CurrencyResponseDto responseDto) {
        LocalDate date = LocalDate.parse(responseDto.getEffectiveDate());
        currencyRateValidator.checkIfUpdateAlreadyExists(date);

        List<CurrencyRate> currencyRates = responseDto.getRates().stream()
                .map(dto -> {
                    CurrencyRate currencyRate = currencyRateMapper.toEntity(dto);
                    currencyRate.setDate(date);
                    return currencyRate;
                })
                .toList();

        currencyRateRepository.saveAll(currencyRates);
        return currencyRates;
    }


    public List<CurrencyRate> findByDate(LocalDate effectiveDate) {
        List<CurrencyRate> currencyRates = currencyRateRepository.findByDate(effectiveDate);
        if (currencyRates.isEmpty()) {
            throw new RuntimeException("No currency rates found for date " + effectiveDate);
        }
        return currencyRates;
    }

    public List<CurrencyRate> findByCode(String code) {
        List<CurrencyRate> currencyRates = currencyRateRepository.findByCode(code);
        if (currencyRates.isEmpty()) {
            throw new RuntimeException("No currency rates found for code " + code);
        }
        return currencyRates;
    }
}