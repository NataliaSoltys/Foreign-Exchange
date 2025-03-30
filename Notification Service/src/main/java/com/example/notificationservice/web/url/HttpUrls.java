package com.example.notificationservice.web.url;

public record HttpUrls() {
    private static final String PROTOCOL = "http";
    private static final String HOST = "localhost";
    private static final int PORT = 8082;
    private static final String PATH_TO_SUBSCRIPTION_CONTROLLER = "/subscriptions";
    private static final String BY_TYPE = "/by-type";
    private static final String QUERY_PARAM_TEMPLATE = "subscriptionType=";
    private static final String GROUPED_BY_TYPE = "/grouped-by-type";

    public static final String GET_SUBSCRIPTIONS_BY_TYPE = PROTOCOL + "://" + HOST + ":" + PORT + PATH_TO_SUBSCRIPTION_CONTROLLER + BY_TYPE + "?" + QUERY_PARAM_TEMPLATE;
    public static final String GET_ALL_SUBSCRIPTIONS_GROUPED_BY_TYPE = PROTOCOL + "://" + HOST + ":" + PORT + PATH_TO_SUBSCRIPTION_CONTROLLER + GROUPED_BY_TYPE;
}
