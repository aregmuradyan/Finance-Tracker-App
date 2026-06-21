package com.aregmuradyan.financetracker.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    private final Button dashboardButton;
    private final Button transactionsButton;
    private final Button exchangeButton;
    private final Button logsButton;
    private final Button analyticsButton;

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

    public Button getAnalyticsButton() {
        return analyticsButton;
    }

    public Sidebar() {
        getStyleClass().add("sidebar");
        dashboardButton = new Button("Dashboard");
        transactionsButton = new Button("Transactions");
        exchangeButton = new Button("Exchange Rates");
        logsButton = new Button("Personal Log");
        analyticsButton = new Button("Analytics");
        Label titleLabel = new Label("FinanceTracker");
        Button collapseButton = new Button("◄");
        setSpacing(10);
        setPadding(new Insets(5));
        setPrefWidth(200);

        HBox header = new HBox();
        header.setSpacing(10);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(
                titleLabel,
                spacer,
                collapseButton
        );
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);
        dashboardButton.setMaxWidth(Double.MAX_VALUE);
        transactionsButton.setMaxWidth(Double.MAX_VALUE);
        exchangeButton.setMaxWidth(Double.MAX_VALUE);
        logsButton.setMaxWidth(Double.MAX_VALUE);
        analyticsButton.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(
                header,
                dashboardButton,
                transactionsButton,
                exchangeButton,
                logsButton,
                analyticsButton
        );
    }
}