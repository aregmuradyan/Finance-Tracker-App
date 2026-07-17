package com.aregmuradyan.financetracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class ExchangeRateService {

    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public ExchangeRateService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.apiKey = System.getenv("EXCHANGE_RATE_API_KEY");
    }

    public Map<String, String> getCurrencies() {
        String url = API_BASE_URL + "/" + getApiKey() + "/codes";

        try {
            JsonNode root = getJson(url);

            Map<String, String> currencies = new TreeMap<>();

            JsonNode supportedCodes = root.get("supported_codes");

            if (supportedCodes != null && supportedCodes.isArray()) {
                for (JsonNode codePair : supportedCodes) {
                    if (codePair.isArray() && codePair.size() >= 2) {
                        String code = codePair.get(0).asText();
                        String name = codePair.get(1).asText();

                        currencies.put(code, name);
                    }
                }
            }

            if (currencies.isEmpty()) {
                addFallbackCurrencies(currencies);
            }

            return currencies;

        } catch (Exception e) {
            Map<String, String> fallback = new TreeMap<>();
            addFallbackCurrencies(fallback);
            return fallback;
        }
    }

    public Map<String, Double> getRates() {
        return getRates("USD");
    }

    public Map<String, Double> getRates(String baseCurrency) {
        String encodedBase = encode(baseCurrency);
        String url = API_BASE_URL + "/" + getApiKey() + "/latest/" + encodedBase;

        try {
            JsonNode root = getJson(url);

            if (!root.has("result") || !root.get("result").asText().equals("success")) {
                throw new RuntimeException("API returned error: " + root);
            }

            Map<String, Double> rates = new TreeMap<>();

            JsonNode conversionRates = root.get("conversion_rates");

            if (conversionRates != null && conversionRates.isObject()) {
                conversionRates.fields().forEachRemaining(entry -> {
                    String quote = entry.getKey();
                    double rate = entry.getValue().asDouble();

                    rates.put(quote, rate);
                });
            }

            return rates;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load exchange rates", e);
        }
    }

    public double getRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }

        String encodedFrom = encode(fromCurrency);
        String encodedTo = encode(toCurrency);

        String url = API_BASE_URL + "/" + getApiKey() + "/pair/" + encodedFrom + "/" + encodedTo;

        try {
            JsonNode root = getJson(url);

            if (!root.has("result") || !root.get("result").asText().equals("success")) {
                throw new RuntimeException("API returned error: " + root);
            }

            return root.get("conversion_rate").asDouble();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load exchange rate", e);
        }
    }

    public double convert(double amount, String fromCurrency, String toCurrency) {
        return amount * getRate(fromCurrency, toCurrency);
    }

    private String getApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Missing EXCHANGE_RATE_API_KEY environment variable");
        }

        return apiKey;
    }

    private JsonNode getJson(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                    "API request failed: " + response.statusCode() + " " + response.body()
            );
        }

        return objectMapper.readTree(response.body());
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private void addFallbackCurrencies(Map<String, String> currencies) {
        currencies.put("AMD", "Armenian Dram");
        currencies.put("USD", "US Dollar");
        currencies.put("EUR", "Euro");
        currencies.put("RUB", "Russian Ruble");
        currencies.put("GBP", "British Pound");
        currencies.put("CHF", "Swiss Franc");
        currencies.put("CAD", "Canadian Dollar");
        currencies.put("AUD", "Australian Dollar");
        currencies.put("JPY", "Japanese Yen");
        currencies.put("CNY", "Chinese Yuan");
    }
}