package com.aregmuradyan.financetracker.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.control.ContentDisplay;

public class Sidebar extends VBox {
    private final Button dashboardButton;
    private final Button transactionsButton;
    private final Button exchangeButton;
    private final Button logsButton;
    private final Button analyticsButton;
    private final Label titleLabel;
    private final Label version;

    private boolean collapsed = false;

    private static final double EXPANDED_WIDTH = 240;
    private static final double COLLAPSED_WIDTH = 76;

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

        FontIcon collapseIcon = new FontIcon("fth-sidebar");
        collapseIcon.getStyleClass().add("sidebar-icon");

        Button collapseButton = new Button();
        collapseButton.setGraphic(collapseIcon);
        collapseButton.getStyleClass().add("collapse-button");
        collapseButton.setOnAction(e -> toggleCollapse());

        setSpacing(20);
        setPadding(new Insets(18));
        setPrefWidth(EXPANDED_WIDTH);
        setMinWidth(EXPANDED_WIDTH);
        setMaxWidth(EXPANDED_WIDTH);

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

        Button button = new Button(text);
        button.setGraphic(icon);
        button.setUserData(text);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.getStyleClass().add("sidebar-button");

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
            setPrefWidth(COLLAPSED_WIDTH);
            setMinWidth(COLLAPSED_WIDTH);
            setMaxWidth(COLLAPSED_WIDTH);

            titleLabel.setVisible(false);
            titleLabel.setManaged(false);

            version.setVisible(false);
            version.setManaged(false);

            for (Button button : buttons) {
                button.setText("");
                button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                button.setAlignment(Pos.CENTER);
            }
        } else {
            setPrefWidth(EXPANDED_WIDTH);
            setMinWidth(EXPANDED_WIDTH);
            setMaxWidth(EXPANDED_WIDTH);

            titleLabel.setVisible(true);
            titleLabel.setManaged(true);

            version.setVisible(true);
            version.setManaged(true);

            for (Button button : buttons) {
                button.setText((String) button.getUserData());
                button.setContentDisplay(ContentDisplay.LEFT);
                button.setAlignment(Pos.CENTER_LEFT);
            }
        }
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
}