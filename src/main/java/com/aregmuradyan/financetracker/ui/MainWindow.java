package com.aregmuradyan.financetracker.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.aregmuradyan.financetracker.ui.views.DashboardView;
import com.aregmuradyan.financetracker.ui.views.ExchangeView;
import com.aregmuradyan.financetracker.ui.views.LogView;
import com.aregmuradyan.financetracker.ui.views.TransactionsView;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        Sidebar sidebar = new Sidebar();

        root.setLeft(sidebar);
        root.setCenter(new DashboardView());
        sidebar.getDashboardButton().setOnAction(e ->
                root.setCenter(new DashboardView()));

        sidebar.getTransactionsButton().setOnAction(e ->
                root.setCenter(new TransactionsView()));

        sidebar.getExchangeButton().setOnAction(e ->
                root.setCenter(new ExchangeView()));

        sidebar.getLogsButton().setOnAction(e ->
                root.setCenter(new LogView()));
        Scene scene = new Scene(root, 1000, 700);

        stage.setTitle("Finance Tracker");
        stage.setScene(scene);
        stage.show();
    }
}