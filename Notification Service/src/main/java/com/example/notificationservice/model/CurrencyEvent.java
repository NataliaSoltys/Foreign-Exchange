package com.example.notificationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyEvent implements Serializable {
    private String table;
    private String no;
    private String effectiveDate;
    private String tradingDate;
    @JsonProperty("rates")
    private List<CurrencyRateDto> rates;

}