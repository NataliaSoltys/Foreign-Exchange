package com.ratesservice.web;

public record HttpUrls() {
    private static final String NBP_PROTOCOL = "https";
    private static final String NBP_HOST = "api.nbp.pl";
    private static final String NBP_PATH = "/api/exchangerates/tables/C";
    private static final String NBP_QUERY_PARAM = "format=json";

    public static final String GET_ALL_EXCHANGE_RATES = NBP_PROTOCOL + "://" + NBP_HOST + NBP_PATH + "?" + NBP_QUERY_PARAM;
}
