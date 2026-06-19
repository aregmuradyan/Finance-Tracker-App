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
import com.aregmuradyan.financetracker.ui.views.AnalyticsView;
import com.aregmuradyan.financetracker.repository.TransactionRepository;
import com.aregmuradyan.financetracker.service.TransactionService;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        Sidebar sidebar = new Sidebar();

        TransactionRepository repository = new TransactionRepository();
        TransactionService service = new TransactionService(repository);
        root.setLeft(sidebar);
        root.setCenter(new DashboardView(service));

        sidebar.getDashboardButton().setOnAction(e ->
                root.setCenter(new DashboardView(service)));

        sidebar.getTransactionsButton().setOnAction(e ->
                root.setCenter(new TransactionsView(service)));

        sidebar.getExchangeButton().setOnAction(e ->
                root.setCenter(new ExchangeView()));

        sidebar.getLogsButton().setOnAction(e ->
                root.setCenter(new LogView()));

        sidebar.getAnalyticsButton().setOnAction(e ->
                root.setCenter(new AnalyticsView()));
        Scene scene = new Scene(root, 1000, 700);

        stage.setTitle("Finance Tracker");
        stage.setScene(scene);
        stage.show();
    }
}