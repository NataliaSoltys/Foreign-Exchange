package com.ratesservice.model.entites;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "currency_rate")
public class CurrencyRate {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "currency_name", nullable = false)
    private String code;

    @Column(name = "buy_price", nullable = false)
    private Float buyPrice;

    @Column(name = "sell_price", nullable = false)
    private Float sellPrice;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}




