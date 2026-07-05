package com.aregmuradyan.financetracker.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import java.util.HashMap;
import java.util.Map;

public class Sidebar extends VBox {
    private final Button dashboardButton;
    private final Button transactionsButton;
    private final Button exchangeButton;
    private final Button logsButton;
    private final Button analyticsButton;
    private final Label titleLabel;
    private final Label version;

    private HBox header;
    private Region headerSpacer;
    private FontIcon collapseIcon;

    private boolean collapsed = false;

    private static final double EXPANDED_WIDTH = 240;
    private static final double COLLAPSED_WIDTH = 76;
    private final Map<Button, Label> buttonTextLabels = new HashMap<>();

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

        dashboardButton = createSidebarButton("Dashboard", "fth-home");
        transactionsButton = createSidebarButton("Transactions", "fth-credit-card");
        analyticsButton = createSidebarButton("Analytics", "fth-pie-chart");
        exchangeButton = createSidebarButton("Exchange Rates", "fth-dollar-sign");
        logsButton = createSidebarButton("Personal Log", "fth-book-open");

        titleLabel = new Label("FinanceTracker");
        titleLabel.getStyleClass().add("sidebar-title");

        collapseIcon = new FontIcon("fth-sidebar");
        collapseIcon.getStyleClass().add("sidebar-icon");

        Button collapseButton = new Button();
        collapseButton.setGraphic(collapseIcon);
        collapseButton.getStyleClass().add("collapse-button");
        collapseButton.setOnMouseEntered(e -> {
            if (collapsed) {
                collapseIcon.setIconLiteral("fth-sidebar");
            }
        });

        collapseButton.setOnMouseExited(e -> {
            if (collapsed) {
                collapseIcon.setIconLiteral("fth-menu");
            }
        });
        collapseButton.setOnAction(e -> toggleCollapse());

        setSpacing(20);

        setPrefWidth(EXPANDED_WIDTH);
        setMinWidth(EXPANDED_WIDTH);
        setMaxWidth(EXPANDED_WIDTH);

        header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        headerSpacer = new Region();
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

        version = new Label("v1.0.0");
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
    private Button createSidebarButton(String text, String iconName) {
        FontIcon icon = new FontIcon(iconName);
        icon.getStyleClass().add("sidebar-icon");

        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("sidebar-button-text");

        HBox content = new HBox(icon, textLabel);
        content.getStyleClass().add("sidebar-button-content");
        content.setSpacing(12);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(content);
        button.setUserData(text);
        button.getStyleClass().add("sidebar-button");
        button.setMaxWidth(Double.MAX_VALUE);

        buttonTextLabels.put(button, textLabel);

        return button;
    }

    private void toggleCollapse() {
        collapsed = !collapsed;

        Button[] buttons = {
                dashboardButton,
                transactionsButton,
                analyticsButton,
                exchangeButton,
                logsButton
        };

        if (collapsed) {
            animateWidth(COLLAPSED_WIDTH);

            titleLabel.setVisible(false);
            titleLabel.setManaged(false);

            version.setVisible(false);
            version.setManaged(false);

            header.setAlignment(Pos.CENTER);

            headerSpacer.setVisible(false);
            headerSpacer.setManaged(false);

            getStyleClass().add("sidebar-collapsed");

            collapseIcon.setIconLiteral("fth-menu");
            for (Button button : buttons) {
                Label label = buttonTextLabels.get(button);

                if (label != null) {
                    fadeNode(label, 0);
                    label.setManaged(false);
                    label.setVisible(false);
                }

                button.setAlignment(Pos.CENTER);
                button.setMaxWidth(Double.MAX_VALUE);
            }
        } else {
            animateWidth(EXPANDED_WIDTH);

            titleLabel.setVisible(true);
            titleLabel.setManaged(true);

            version.setVisible(true);
            version.setManaged(true);

            header.setAlignment(Pos.CENTER_LEFT);

            headerSpacer.setVisible(true);
            headerSpacer.setManaged(true);

            getStyleClass().remove("sidebar-collapsed");


            collapseIcon.setIconLiteral("fth-sidebar");

            for (Button button : buttons) {
                Label label = buttonTextLabels.get(button);

                if (label != null) {
                    label.setVisible(true);
                    label.setManaged(true);
                    fadeNode(label, 1);
                }

                button.setAlignment(Pos.CENTER_LEFT);
                button.setMaxWidth(Double.MAX_VALUE);
            }
        }
    }

    private void animateWidth(double targetWidth) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(180),
                        new KeyValue(prefWidthProperty(), targetWidth),
                        new KeyValue(minWidthProperty(), targetWidth),
                        new KeyValue(maxWidthProperty(), targetWidth)
                )
        );

        timeline.play();
    }

    public void setActiveButton(Button activeButton) {
        Button[] buttons = {
                dashboardButton,
                transactionsButton,
                analyticsButton,
                exchangeButton,
                logsButton
        };

        for (Button button : buttons) {
            button.getStyleClass().remove("sidebar-button-active");
        }

        activeButton.getStyleClass().add("sidebar-button-active");
    }

    private void fadeNode(javafx.scene.Node node, double targetOpacity) {
        FadeTransition fade = new FadeTransition(Duration.millis(120), node);
        fade.setToValue(targetOpacity);
        fade.play();
    }
}