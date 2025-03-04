package com.ratesservice.repository;

import com.ratesservice.model.entites.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, UUID> {

    List<CurrencyRate> findByCode(String code);

    List<CurrencyRate> findByDate(LocalDate date);
}
