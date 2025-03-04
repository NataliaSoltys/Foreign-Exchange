package com.ratesservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CurrencyRateDto implements Serializable {

    @JsonProperty("currency")
    private String currencyName;

    @JsonProperty("code")
    private String code;

    @JsonProperty("bid")
    private Float buyPrice;

    @JsonProperty("ask")
    private Float sellPrice;
}
