package com.ratesservice.web;

import com.ratesservice.model.entites.CurrencyRate;
import com.ratesservice.repository.CurrencyRateRepository;
import com.ratesservice.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/currency-rates")
public class CurrencyRatesController {

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    private static Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    @GetMapping("/by-date")
    public ResponseEntity<List<CurrencyRate>> getCurrencyRatesByDate(@RequestParam("date") String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            List<CurrencyRate> currencyRates = currencyRateRepository.findByDate(parsedDate);
            if (currencyRates.isEmpty()) {
                logger.info("No currency rates found for date {}", date);
                return ResponseEntity.noContent().build();
            }
            logger.info("Fetched currency rates {} records", currencyRates.size());
            return ResponseEntity.ok(currencyRates);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/by-code")
    public ResponseEntity<List<CurrencyRate>> getCurrencyRatesByCode(@RequestParam("code") String code) {
        List<CurrencyRate> currencyRates = currencyRateRepository.findByCode(code);
        if (currencyRates.isEmpty()) {
            logger.info("No currency rates found for code {}", code);
            return ResponseEntity.noContent().build();
        }
        logger.info("Fetched currency rates {} records", currencyRates.size());
        return ResponseEntity.ok(currencyRates);
    }
}
