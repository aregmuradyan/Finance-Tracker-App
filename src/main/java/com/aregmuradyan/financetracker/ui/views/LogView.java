package com.aregmuradyan.financetracker.ui.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LogView extends VBox {

    public LogView() {
        getChildren().add(new Label("Personal Log"));
    }
}