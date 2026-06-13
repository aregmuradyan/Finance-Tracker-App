package com.aregmuradyan.financetracker.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        Sidebar sidebar = new Sidebar();

        root.setLeft(sidebar);
        root.setCenter(new Label("Dashboard"));

        Scene scene = new Scene(root, 1000, 700);

        stage.setTitle("Finance Tracker");
        stage.setScene(scene);
        stage.show();
    }
}