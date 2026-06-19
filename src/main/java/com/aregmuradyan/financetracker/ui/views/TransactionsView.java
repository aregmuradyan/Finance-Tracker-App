package com.aregmuradyan.financetracker.ui.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TransactionsView extends VBox {

    public TransactionsView() {
        getChildren().add(new Label("Transactions"));
    }
}