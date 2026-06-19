package com.aregmuradyan.financetracker.ui.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView extends VBox {

    public DashboardView() {
        getChildren().add(new Label("Dashboard"));
    }
}