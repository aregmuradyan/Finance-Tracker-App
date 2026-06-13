package com.aregmuradyan.financetracker.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    public Sidebar() {
        setSpacing(10);
        setPadding(new Insets(20));
        setPrefWidth(200);

        Button dashboardButton = new Button("Dashboard");
        Button transactionsButton = new Button("Transactions");
        Button exchangeButton = new Button("Exchange Rates");
        Button logsButton = new Button("Personal Log");

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