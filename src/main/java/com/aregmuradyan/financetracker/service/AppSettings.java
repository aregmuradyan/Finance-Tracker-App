package com.aregmuradyan.financetracker.service;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AppSettings {

    private final StringProperty selectedCurrency = new SimpleStringProperty("USD");
    private final BooleanProperty darkMode = new SimpleBooleanProperty(false);

    public String getSelectedCurrency() {
        return selectedCurrency.get();
    }

    public void setSelectedCurrency(String currency) {
        selectedCurrency.set(currency);
    }

    public StringProperty selectedCurrencyProperty() {
        return selectedCurrency;
    }

    public boolean isDarkMode() {
        return darkMode.get();
    }

    public void setDarkMode(boolean enabled) {
        darkMode.set(enabled);
    }

    public BooleanProperty darkModeProperty() {
        return darkMode;
    }
}