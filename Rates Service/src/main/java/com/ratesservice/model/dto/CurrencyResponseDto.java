package com.ratesservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
public class CurrencyResponseDto implements Serializable {
    private String table;
    private String no;
    private String effectiveDate;
    private String tradingDate;
    @JsonProperty("rates")
    private List<CurrencyRateDto> rates;

}