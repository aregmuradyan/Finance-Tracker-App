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

    private static final String API_BASE_URL = "https://api.frankfurter.dev/v2";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ExchangeRateService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, String> getCurrencies() {
        String url = API_BASE_URL + "/currencies";

        try {
            JsonNode root = getJson(url);

            Map<String, String> currencies = new TreeMap<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    String code = node.has("code")
                            ? node.get("code").asText()
                            : null;

                    String name = node.has("name")
                            ? node.get("name").asText()
                            : code;

                    if (code != null && !code.isBlank()) {
                        currencies.put(code, name);
                    }
                }
            } else if (root.isObject()) {
                root.fields().forEachRemaining(entry -> {
                    String code = entry.getKey();
                    JsonNode value = entry.getValue();

                    String name;

                    if (value.isTextual()) {
                        name = value.asText();
                    } else if (value.has("name")) {
                        name = value.get("name").asText();
                    } else {
                        name = code;
                    }

                    currencies.put(code, name);
                });
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
        String url = API_BASE_URL + "/rates?base=" + encodedBase;

        try {
            JsonNode root = getJson(url);

            Map<String, Double> rates = new TreeMap<>();

            if (root.isArray()) {
                for (JsonNode node : root) {
                    if (node.has("quote") && node.has("rate")) {
                        String quote = node.get("quote").asText();
                        double rate = node.get("rate").asDouble();

                        rates.put(quote, rate);
                    }
                }
            } else if (root.isObject()) {
                if (root.has("rates")) {
                    JsonNode ratesNode = root.get("rates");

                    ratesNode.fields().forEachRemaining(entry -> {
                        String quote = entry.getKey();
                        double rate = entry.getValue().asDouble();

                        rates.put(quote, rate);
                    });
                }
            }

            rates.put(baseCurrency, 1.0);

            return rates;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load exchange rates", e);
        }
    }

    public double getRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }

        try {
            Map<String, Double> rates = getRates(fromCurrency);

            Double rate = rates.get(toCurrency);

            if (rate != null) {
                return rate;
            }
        } catch (Exception ignored) {
        }

        String encodedFrom = encode(fromCurrency);
        String encodedTo = encode(toCurrency);

        String url = API_BASE_URL + "/rate/" + encodedFrom + "/" + encodedTo;

        try {
            JsonNode root = getJson(url);

            if (root.has("rate")) {
                return root.get("rate").asDouble();
            }

            if (root.isArray() && !root.isEmpty()) {
                JsonNode firstRate = root.get(0);

                if (firstRate.has("rate")) {
                    return firstRate.get("rate").asDouble();
                }
            }

            if (root.has("rates") && root.get("rates").has(toCurrency)) {
                return root.get("rates").get(toCurrency).asDouble();
            }

            throw new RuntimeException("Exchange rate not found");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load exchange rate", e);
        }
    }

    public double convert(double amount, String fromCurrency, String toCurrency) {
        return amount * getRate(fromCurrency, toCurrency);
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