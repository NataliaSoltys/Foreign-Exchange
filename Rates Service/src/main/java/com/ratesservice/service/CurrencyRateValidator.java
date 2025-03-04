package com.ratesservice.service;

import com.ratesservice.repository.CurrencyRateRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CurrencyRateValidator {

    private final CurrencyRateRepository currencyRateRepository;

    @SneakyThrows
    public void checkIfUpdateAlreadyExists(LocalDate date) {
        if (!currencyRateRepository.findByDate(date).isEmpty()) {
            throw new DataIntegrityViolationException("Currency rates for this date already exist");
        }
    }
}
