package com.aregmuradyan.financetracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Double> getRates() {
        Map<String, Double> rates = new HashMap<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.frankfurter.dev/v2/rates?base=USD&quotes=AMD,EUR,RUB"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            JsonNode root = objectMapper.readTree(response.body());
            for (JsonNode node : root) {
                String quote = node.get("quote").asText();
                double rate = node.get("rate").asDouble();

                rates.put(quote, rate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rates;
    }
}