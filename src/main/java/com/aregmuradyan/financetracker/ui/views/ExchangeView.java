package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.service.ExchangeRateService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;

public class ExchangeView extends VBox {

    private final ExchangeRateService service;

    public ExchangeView(ExchangeRateService service) {

        this.service = service;

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Exchange Rates",
                "View live currency exchange rates"
        );

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


        VBox card = new VBox();
        card.getStyleClass().add("content-card");
        card.getChildren().addAll(
                header,
                amdLabel,
                eurLabel,
                rubLabel,
                refreshButton
        );
        getChildren().addAll(
                header,
                card
        );

    }
}