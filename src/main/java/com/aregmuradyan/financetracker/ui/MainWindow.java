package com.aregmuradyan.financetracker.ui;

import com.aregmuradyan.financetracker.repository.LogRepository;
import com.aregmuradyan.financetracker.repository.TransactionRepository;
import com.aregmuradyan.financetracker.service.ExchangeRateService;
import com.aregmuradyan.financetracker.service.LogService;
import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.service.AppSettings;
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
import javafx.animation.FadeTransition;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainWindow extends Application {

    private BorderPane root;
    private Sidebar sidebar;
    private BorderPane mainContent;
    private StackPane appContainer;

    private final AppSettings settings = new AppSettings();

    private ScrollPane dashboardPane;
    private ScrollPane transactionsPane;
    private ScrollPane analyticsPane;
    private ScrollPane exchangePane;
    private ScrollPane logPane;

    private DashboardView dashboardView;
    private AnalyticsView analyticsView;

    private ExchangeRateService exchangeRateService;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        appContainer = new StackPane(root);
        mainContent = new BorderPane();

        sidebar = new Sidebar();


        TransactionRepository repository = new TransactionRepository();
        TransactionService service = new TransactionService(repository);

        LogRepository logRepository = new LogRepository();
        LogService logService = new LogService(logRepository);

        exchangeRateService = new ExchangeRateService();

        createViews(service, logService, exchangeRateService);
        setupLayout();
        setupNavigation();

        Scene scene = new Scene(appContainer, 1280, 780);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );

        stage.setTitle("Finance Tracker");
        stage.setScene(scene);

        stage.setMinWidth(1100);
        stage.setMinHeight(700);

        stage.show();
    }

    private void createViews(
            TransactionService service,
            LogService logService,
            ExchangeRateService exchangeRateService
    ) {
        dashboardView = new DashboardView(service);
        TransactionsView transactionsView = new TransactionsView(service, settings);
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
        TopBar topBar = new TopBar(settings, exchangeRateService);
        mainContent.getStyleClass().add("main-content");
        settings.darkModeProperty().addListener((obs, oldValue, newValue) -> {
            animateThemeChange(newValue);
        });

        mainContent.setTop(topBar);
        mainContent.setCenter(dashboardPane);

        root.setLeft(sidebar);
        root.setCenter(mainContent);

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
        mainContent.setCenter(pane);
        sidebar.setActiveButton(activeButton);
    }

    private ScrollPane createScrollPane(javafx.scene.Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.getStyleClass().add("main-scroll-pane");
        return scrollPane;
    }
    private void animateThemeChange(boolean darkMode) {
        WritableImage snapshot = root.snapshot(null, null);
        ImageView oldThemeImage = new ImageView(snapshot);

        oldThemeImage.setFitWidth(root.getWidth());
        oldThemeImage.setFitHeight(root.getHeight());

        appContainer.getChildren().add(oldThemeImage);

        if (darkMode) {
            root.getStyleClass().add("dark-mode");
        } else {
            root.getStyleClass().remove("dark-mode");
        }

        FadeTransition fade = new FadeTransition(Duration.millis(220), oldThemeImage);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.setOnFinished(e -> {
            appContainer.getChildren().remove(oldThemeImage);
        });

        fade.play();
    }
}