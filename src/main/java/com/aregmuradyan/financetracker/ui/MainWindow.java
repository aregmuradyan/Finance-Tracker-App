package com.aregmuradyan.financetracker.ui;

import com.aregmuradyan.financetracker.repository.LogRepository;
import com.aregmuradyan.financetracker.repository.TransactionRepository;
import com.aregmuradyan.financetracker.service.ExchangeRateService;
import com.aregmuradyan.financetracker.service.LogService;
import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.ui.views.AnalyticsView;
import com.aregmuradyan.financetracker.ui.views.DashboardView;
import com.aregmuradyan.financetracker.ui.views.ExchangeView;
import com.aregmuradyan.financetracker.ui.views.LogView;
import com.aregmuradyan.financetracker.ui.views.TransactionsView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends Application {

    private BorderPane root;
    private Sidebar sidebar;

    private ScrollPane dashboardPane;
    private ScrollPane transactionsPane;
    private ScrollPane analyticsPane;
    private ScrollPane exchangePane;
    private ScrollPane logPane;

    private DashboardView dashboardView;
    private AnalyticsView analyticsView;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();

        sidebar = new Sidebar();
        sidebar.prefWidthProperty().bind(root.widthProperty().multiply(0.20));
        sidebar.setMinWidth(220);
        sidebar.setMaxWidth(280);

        TransactionRepository repository = new TransactionRepository();
        TransactionService service = new TransactionService(repository);

        LogRepository logRepository = new LogRepository();
        LogService logService = new LogService(logRepository);

        ExchangeRateService exchangeRateService = new ExchangeRateService();

        createViews(service, logService, exchangeRateService);
        setupLayout();
        setupNavigation();

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Finance Tracker");
        stage.setScene(scene);
        stage.show();
    }

    private void createViews(
            TransactionService service,
            LogService logService,
            ExchangeRateService exchangeRateService
    ) {
        dashboardView = new DashboardView(service);
        TransactionsView transactionsView = new TransactionsView(service);
        analyticsView = new AnalyticsView(service);
        ExchangeView exchangeView = new ExchangeView(exchangeRateService);
        LogView logView = new LogView(logService);

        dashboardPane = createScrollPane(dashboardView);
        transactionsPane = createScrollPane(transactionsView);
        analyticsPane = createScrollPane(analyticsView);
        exchangePane = createScrollPane(exchangeView);
        logPane = createScrollPane(logView);
    }

    private void setupLayout() {
        root.setLeft(sidebar);
        root.setCenter(dashboardPane);
        sidebar.setActiveButton(sidebar.getDashboardButton());
    }

    private void setupNavigation() {
        sidebar.getDashboardButton().setOnAction(e -> {
            dashboardView.refresh();
            switchView(dashboardPane, sidebar.getDashboardButton());
        });

        sidebar.getTransactionsButton().setOnAction(e ->
                switchView(transactionsPane, sidebar.getTransactionsButton())
        );

        sidebar.getAnalyticsButton().setOnAction(e -> {
            analyticsView.refresh();
            switchView(analyticsPane, sidebar.getAnalyticsButton());
        });

        sidebar.getExchangeButton().setOnAction(e ->
                switchView(exchangePane, sidebar.getExchangeButton())
        );

        sidebar.getLogsButton().setOnAction(e ->
                switchView(logPane, sidebar.getLogsButton())
        );
    }

    private void switchView(ScrollPane pane, Button activeButton) {
        root.setCenter(pane);
        sidebar.setActiveButton(activeButton);
    }

    private ScrollPane createScrollPane(javafx.scene.Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.getStyleClass().add("main-scroll-pane");
        return scrollPane;
    }
}