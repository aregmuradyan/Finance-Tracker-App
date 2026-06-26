package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.service.ExchangeRateService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;

public class ExchangeView extends VBox {

    private final ExchangeRateService service;

    public ExchangeView(ExchangeRateService service) {

        this.service = service;

        Label title = new Label("Exchange Rates");

        Label amdLabel = new Label();
        Label eurLabel = new Label();
        Label rubLabel = new Label();

        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("action-button");

        refreshButton.setOnAction(e -> {

            Map<String, Double> rates = service.getRates();

            amdLabel.setText("USD → AMD : " + rates.get("AMD"));
            eurLabel.setText("USD → EUR : " + rates.get("EUR"));
            rubLabel.setText("USD → RUB : " + rates.get("RUB"));

        });

        refreshButton.fire();

        getChildren().addAll(
                title,
                amdLabel,
                eurLabel,
                rubLabel,
                refreshButton
        );

    }
}