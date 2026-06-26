package com.aregmuradyan.financetracker.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        titleLabel.getStyleClass().add("sidebar-title");

        var iconUrl = getClass().getResource("/icons/sidebar_collapse.png");

        System.out.println("ICON URL = " + iconUrl);

        if (iconUrl == null) {
            throw new RuntimeException("Icon not found");
        }

        Image collapseImage = new Image(iconUrl.toExternalForm());

        System.out.println("IMAGE ERROR = " + collapseImage.isError());

        ImageView collapseIcon = new ImageView(collapseImage);
        collapseIcon.setFitWidth(22);
        collapseIcon.setFitHeight(22);
        collapseIcon.setPreserveRatio(true);

        Button collapseButton = new Button();
        collapseButton.setGraphic(collapseIcon);
        collapseButton.getStyleClass().add("collapse-button");


        setSpacing(20);
        setPadding(new Insets(18));

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        header.getChildren().addAll(
                titleLabel,
                headerSpacer,
                collapseButton
        );

        Button[] buttons = {
                dashboardButton,
                transactionsButton,
                analyticsButton,
                exchangeButton,
                logsButton
        };

        for (Button button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.getStyleClass().add("sidebar-button");
        }

        VBox navigation = new VBox();
        navigation.setSpacing(10);
        navigation.getChildren().addAll(
                dashboardButton,
                transactionsButton,
                analyticsButton,
                exchangeButton,
                logsButton
        );

        Region footerSpacer = new Region();
        VBox.setVgrow(footerSpacer, Priority.ALWAYS);

        Label version = new Label("v1.0.0");
        version.getStyleClass().add("sidebar-version");

        VBox footer = new VBox(version);
        footer.getStyleClass().add("sidebar-footer");

        getChildren().addAll(
                header,
                navigation,
                footerSpacer,
                footer
        );
    }
}