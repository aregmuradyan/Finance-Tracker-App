package com.aregmuradyan.financetracker.ui.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ExchangeView extends VBox {

    public ExchangeView() {
        getChildren().add(new Label("Exchange Rates"));
    }
}