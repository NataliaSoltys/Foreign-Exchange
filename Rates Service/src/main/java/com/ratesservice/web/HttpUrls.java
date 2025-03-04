package com.ratesservice.web;

public record HttpUrls() {
    public static String getAllExchangeRates = "https://api.nbp.pl/api/exchangerates/tables/C?format=json";
}
