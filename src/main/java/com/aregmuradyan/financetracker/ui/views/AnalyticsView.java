package com.aregmuradyan.financetracker.ui.views;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AnalyticsView extends VBox {

    public AnalyticsView() {
        setSpacing(15);
        setPadding(new Insets(20));

        getChildren().addAll(
                new Label("Analytics"),
                new Label("Charts will go here")
        );
    }
}