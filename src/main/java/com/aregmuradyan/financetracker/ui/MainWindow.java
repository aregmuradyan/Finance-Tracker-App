package com.aregmuradyan.financetracker.ui;

import com.aregmuradyan.financetracker.repository.LogRepository;
import com.aregmuradyan.financetracker.service.ExchangeRateService;
import com.aregmuradyan.financetracker.service.LogService;
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
import javafx.scene.control.ScrollPane;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        Sidebar sidebar = new Sidebar();
        sidebar.prefWidthProperty().bind(
                root.widthProperty().multiply(0.20)
        );

        sidebar.setMinWidth(220);
        sidebar.setMaxWidth(280);

        TransactionRepository repository = new TransactionRepository();
        TransactionService service = new TransactionService(repository);
        LogRepository logRepository = new LogRepository();
        LogService logService = new LogService(logRepository);
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        root.setLeft(sidebar);
        root.setCenter(new DashboardView(service));

        sidebar.getDashboardButton().setOnAction(e -> {
            sidebar.setActiveButton(sidebar.getDashboardButton());
            root.setCenter(createScrollPane(new DashboardView(service)));
        });
        root.setCenter(createScrollPane(new DashboardView(service)));
        sidebar.setActiveButton(sidebar.getDashboardButton());

        sidebar.getTransactionsButton().setOnAction(e -> {
            sidebar.setActiveButton(sidebar.getTransactionsButton());
            root.setCenter(createScrollPane(new TransactionsView(service)));
        });

        sidebar.getExchangeButton().setOnAction(e -> {
            sidebar.setActiveButton(sidebar.getExchangeButton());
            root.setCenter(createScrollPane(new ExchangeView(exchangeRateService)));
        });

        sidebar.getLogsButton().setOnAction(e -> {
            sidebar.setActiveButton(sidebar.getLogsButton());
            root.setCenter(createScrollPane(new LogView(logService)));
        });

        sidebar.getAnalyticsButton().setOnAction(e -> {
            sidebar.setActiveButton(sidebar.getAnalyticsButton());
            root.setCenter(createScrollPane(new AnalyticsView(service)));
        });
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );
        stage.setTitle("Finance Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private ScrollPane createScrollPane(javafx.scene.Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.getStyleClass().add("main-scroll-pane");
        return scrollPane;
    }
}