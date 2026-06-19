package com.aregmuradyan.financetracker.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    private final Button dashboardButton;
    private final Button transactionsButton;
    private final Button exchangeButton;
    private final Button logsButton;

    public Button getDashboardButton() {
        return dashboardButton;
    }

    public Button getTransactionsButton() {
        return transactionsButton;
    }

    public Button getExchangeButton() {
        return exchangeButton;
    }

    public Button getLogsButton() {
        return logsButton;
    }

    public Sidebar() {
        dashboardButton = new Button("Dashboard");
        transactionsButton = new Button("Transactions");
        exchangeButton = new Button("Exchange Rates");
        logsButton = new Button("Personal Log");
        setSpacing(10);
        setPadding(new Insets(20));
        setPrefWidth(200);

        dashboardButton.setMaxWidth(Double.MAX_VALUE);
        transactionsButton.setMaxWidth(Double.MAX_VALUE);
        exchangeButton.setMaxWidth(Double.MAX_VALUE);
        logsButton.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(
                dashboardButton,
                transactionsButton,
                exchangeButton,
                logsButton
        );
    }
}