package com.aregmuradyan.financetracker.ui;

import com.aregmuradyan.financetracker.service.AppSettings;
import com.aregmuradyan.financetracker.service.ExchangeRateService;
import com.aregmuradyan.financetracker.ui.helper.SearchableComboBoxHelper;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.application.Platform;

public class TopBar extends HBox {

    private final AppSettings settings;
    private final ExchangeRateService exchangeRateService;

    public TopBar(AppSettings settings, ExchangeRateService exchangeRateService) {
        this.settings = settings;
        this.exchangeRateService = exchangeRateService;

        getStyleClass().add("top-bar");
        setAlignment(Pos.CENTER_RIGHT);
        setSpacing(10);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label currencyLabel = new Label("Currency");
        currencyLabel.getStyleClass().add("top-bar-label");

        ComboBox<String> currencyBox = new ComboBox<>();
        currencyBox.getStyleClass().add("top-bar-combo");

        try {
            currencyBox.getItems().addAll(exchangeRateService.getCurrencies().keySet());
        } catch (Exception ex) {
            currencyBox.getItems().addAll(
                    "AMD",
                    "USD",
                    "EUR",
                    "RUB",
                    "GBP",
                    "CHF",
                    "CAD",
                    "AUD",
                    "JPY",
                    "CNY"
            );
        }

        currencyBox.setValue(settings.getSelectedCurrency());

        SearchableComboBoxHelper.makeSearchable(currencyBox);

        currencyBox.getSelectionModel().selectedItemProperty().addListener((obs, oldCurrency, newCurrency) -> {
            if (newCurrency != null) {
                settings.setSelectedCurrency(newCurrency);
            }
        });

        FontIcon themeIcon = new FontIcon("fth-sun");
        themeIcon.getStyleClass().add("top-bar-icon");

        ToggleButton themeToggle = new ToggleButton();
        themeToggle.setGraphic(themeIcon);
        themeToggle.getStyleClass().add("theme-toggle");

        themeToggle.setOnAction(e -> {
            boolean darkMode = themeToggle.isSelected();

            settings.setDarkMode(darkMode);

            if (darkMode) {
                themeIcon.setIconLiteral("fth-moon");
            } else {
                themeIcon.setIconLiteral("fth-sun");
            }
        });

        Button quickAddButton = createIconButton("fth-plus");
        Button moreButton = createIconButton("fth-more-horizontal");

        quickAddButton.setOnAction(e -> {
            System.out.println("Quick add clicked");
        });

        moreButton.setOnAction(e -> {
            System.out.println("More clicked");
        });

        getChildren().addAll(
                spacer,
                currencyLabel,
                currencyBox,
                themeToggle,
                quickAddButton,
                moreButton
        );
    }

    private Button createIconButton(String iconName) {
        FontIcon icon = new FontIcon(iconName);
        icon.getStyleClass().add("top-bar-icon");

        Button button = new Button();
        button.setGraphic(icon);
        button.getStyleClass().add("top-bar-icon-button");

        return button;
    }
}