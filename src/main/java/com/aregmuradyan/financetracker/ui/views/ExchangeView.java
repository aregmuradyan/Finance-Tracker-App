package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.service.ExchangeRateService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Map;

public class ExchangeView extends VBox {

    private final ExchangeRateService service;

    private final TextField amountField;
    private final ComboBox<String> fromCurrencyBox;
    private final ComboBox<String> toCurrencyBox;
    private final Label resultLabel;
    private final Label rateLabel;
    private final Label statusLabel;
    private final VBox quickRatesBox;
    private final Button convertButton;
    private final Button refreshButton;

    public ExchangeView(ExchangeRateService service) {
        this.service = service;

        getStyleClass().add("page");

        PageHeader header = new PageHeader(
                "Exchange Rates",
                "Convert currencies using live exchange rates"
        );

        amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setText("100");

        fromCurrencyBox = new ComboBox<>();
        fromCurrencyBox.setVisibleRowCount(8);

        toCurrencyBox = new ComboBox<>();
        toCurrencyBox.setVisibleRowCount(8);

        resultLabel = new Label("Loading exchange rates...");
        resultLabel.getStyleClass().add("exchange-result-main");

        rateLabel = new Label("Select currencies and convert");
        rateLabel.getStyleClass().add("exchange-rate-subtitle");

        statusLabel = new Label();
        statusLabel.getStyleClass().add("exchange-status");

        convertButton = new Button("Convert");
        convertButton.getStyleClass().add("action-button");
        convertButton.getStyleClass().add("primary-action-button");

        refreshButton = new Button("Refresh Rates");
        refreshButton.getStyleClass().add("action-button");
        refreshButton.getStyleClass().add("secondary-action-button");

        quickRatesBox = new VBox();
        quickRatesBox.getStyleClass().add("quick-rates-list");

        amountField.setMaxWidth(Double.MAX_VALUE);
        fromCurrencyBox.setMaxWidth(Double.MAX_VALUE);
        toCurrencyBox.setMaxWidth(Double.MAX_VALUE);

        convertButton.setOnAction(e -> convertAmount());
        refreshButton.setOnAction(e -> loadQuickRates());

        fromCurrencyBox.setOnAction(e -> loadQuickRates());

        VBox converterCard = createConverterCard();
        VBox quickRatesCard = createQuickRatesCard();

        getChildren().addAll(
                header,
                converterCard,
                quickRatesCard
        );

        loadCurrencies();
    }

    private VBox createConverterCard() {
        Label cardTitle = new Label("Currency Converter");
        cardTitle.getStyleClass().add("section-title");

        GridPane converterGrid = new GridPane();
        converterGrid.getStyleClass().add("exchange-grid");
        converterGrid.setHgap(12);
        converterGrid.setVgap(12);

        converterGrid.add(amountField, 0, 0);
        converterGrid.add(fromCurrencyBox, 1, 0);
        converterGrid.add(toCurrencyBox, 2, 0);
        converterGrid.add(convertButton, 3, 0);

        HBox resultCard = new HBox();
        resultCard.getStyleClass().add("exchange-result-card");
        resultCard.setAlignment(Pos.CENTER_LEFT);

        VBox resultTextBox = new VBox();
        resultTextBox.getStyleClass().add("exchange-result-text");
        resultTextBox.getChildren().addAll(
                resultLabel,
                rateLabel
        );

        resultCard.getChildren().add(resultTextBox);

        VBox converterCard = new VBox();
        converterCard.getStyleClass().add("content-card");
        converterCard.getStyleClass().add("exchange-card");

        converterCard.getChildren().addAll(
                cardTitle,
                converterGrid,
                resultCard,
                statusLabel
        );

        return converterCard;
    }

    private VBox createQuickRatesCard() {
        Label cardTitle = new Label("Quick Rates");
        cardTitle.getStyleClass().add("section-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox quickRatesHeader = new HBox();
        quickRatesHeader.getStyleClass().add("exchange-header-row");
        quickRatesHeader.setAlignment(Pos.CENTER_LEFT);
        quickRatesHeader.getChildren().addAll(
                cardTitle,
                spacer,
                refreshButton
        );

        VBox quickRatesCard = new VBox();
        quickRatesCard.getStyleClass().add("content-card");
        quickRatesCard.getStyleClass().add("exchange-card");

        quickRatesCard.getChildren().addAll(
                quickRatesHeader,
                quickRatesBox
        );

        return quickRatesCard;
    }

    private void loadCurrencies() {
        setLoading(true);
        statusLabel.setText("Loading currencies...");

        Task<Map<String, String>> task = new Task<>() {
            @Override
            protected Map<String, String> call() {
                return service.getCurrencies();
            }
        };

        task.setOnSucceeded(e -> {
            Map<String, String> currencies = task.getValue();

            fromCurrencyBox.getItems().setAll(currencies.keySet());
            toCurrencyBox.getItems().setAll(currencies.keySet());

            fromCurrencyBox.setValue("USD");
            toCurrencyBox.setValue("AMD");

            statusLabel.setText("Currencies loaded");

            setLoading(false);
            convertAmount();
            loadQuickRates();
        });

        task.setOnFailed(e -> {
            statusLabel.setText("Could not load currencies");
            resultLabel.setText("Exchange rates unavailable");
            rateLabel.setText("Please try again later");
            setLoading(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void convertAmount() {
        String amountText = amountField.getText();
        String fromCurrency = fromCurrencyBox.getValue();
        String toCurrency = toCurrencyBox.getValue();

        if (amountText == null || amountText.isBlank()) {
            statusLabel.setText("Enter an amount");
            return;
        }

        if (fromCurrency == null || toCurrency == null) {
            statusLabel.setText("Select currencies");
            return;
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            statusLabel.setText("Amount must be a number");
            return;
        }

        setLoading(true);
        statusLabel.setText("Converting...");

        Task<Double> task = new Task<>() {
            @Override
            protected Double call() {
                return service.convert(amount, fromCurrency, toCurrency);
            }
        };

        task.setOnSucceeded(e -> {
            double convertedAmount = task.getValue();
            double rate = amount == 0 ? 0 : convertedAmount / amount;

            resultLabel.setText(
                    String.format("%,.2f %s = %,.2f %s",
                            amount,
                            fromCurrency,
                            convertedAmount,
                            toCurrency
                    )
            );

            rateLabel.setText(
                    String.format("1 %s = %,.6f %s",
                            fromCurrency,
                            rate,
                            toCurrency
                    )
            );

            statusLabel.setText("Updated with live rate");
            setLoading(false);
        });

        task.setOnFailed(e -> {
            resultLabel.setText("Conversion failed");
            rateLabel.setText("Could not load rate");
            statusLabel.setText("Please try again");
            setLoading(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void loadQuickRates() {
        String fromCurrency = fromCurrencyBox.getValue();

        if (fromCurrency == null) {
            return;
        }

        quickRatesBox.getChildren().clear();

        Label loadingLabel = new Label("Loading quick rates...");
        loadingLabel.getStyleClass().add("exchange-status");
        quickRatesBox.getChildren().add(loadingLabel);

        Task<Map<String, Double>> task = new Task<>() {
            @Override
            protected Map<String, Double> call() {
                return service.getRates(fromCurrency);
            }
        };

        task.setOnSucceeded(e -> {
            Map<String, Double> rates = task.getValue();

            quickRatesBox.getChildren().clear();

            addQuickRateRow(fromCurrency, "AMD", rates);
            addQuickRateRow(fromCurrency, "USD", rates);
            addQuickRateRow(fromCurrency, "EUR", rates);
            addQuickRateRow(fromCurrency, "RUB", rates);
            addQuickRateRow(fromCurrency, "GBP", rates);
            addQuickRateRow(fromCurrency, "CHF", rates);
            addQuickRateRow(fromCurrency, "CAD", rates);
        });

        task.setOnFailed(e -> {
            quickRatesBox.getChildren().clear();

            Label errorLabel = new Label("Could not load quick rates");
            errorLabel.getStyleClass().add("exchange-status");

            quickRatesBox.getChildren().add(errorLabel);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void addQuickRateRow(String fromCurrency, String toCurrency, Map<String, Double> rates) {
        if (fromCurrency.equals(toCurrency)) {
            return;
        }

        Double rate = rates.get(toCurrency);

        if (rate == null) {
            return;
        }

        Label pairLabel = new Label(fromCurrency + " → " + toCurrency);
        pairLabel.getStyleClass().add("quick-rate-pair");

        Label valueLabel = new Label(String.format("%,.6f", rate));
        valueLabel.getStyleClass().add("quick-rate-value");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox();
        row.getStyleClass().add("quick-rate-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.getChildren().addAll(
                pairLabel,
                spacer,
                valueLabel
        );

        quickRatesBox.getChildren().add(row);
    }

    private void setLoading(boolean loading) {
        Platform.runLater(() -> {
            convertButton.setDisable(loading);
            refreshButton.setDisable(loading);
            amountField.setDisable(loading);
            fromCurrencyBox.setDisable(loading);
            toCurrencyBox.setDisable(loading);
        });
    }
}