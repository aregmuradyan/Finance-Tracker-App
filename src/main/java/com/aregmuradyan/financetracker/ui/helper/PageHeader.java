package com.aregmuradyan.financetracker.ui.helper;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PageHeader extends VBox {

    public PageHeader(String titleText, String subtitleText) {
        getStyleClass().add("page-header");

        Label title = new Label(titleText);
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(subtitleText);
        subtitle.getStyleClass().add("page-subtitle");

        getChildren().addAll(title, subtitle);
    }
}