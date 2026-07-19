package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import com.aregmuradyan.financetracker.model.Category;
import com.aregmuradyan.financetracker.service.AppSettings;
import com.aregmuradyan.financetracker.service.ExchangeRateService;
import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.model.TransactionType;

import java.util.EnumMap;
import java.util.Map;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.scene.paint.Color;
import javafx.scene.control.ComboBox;
import javafx.geometry.Pos;

public class AnalyticsView extends VBox {

    private final TransactionService service;
    private final AppSettings settings;
    private final ExchangeRateService exchangeRateService;

    private PieChart expensePieChart;
    private BarChart<String, Number> expenseBarChart;

    private final Popup piePopup = new Popup();
    private VBox piePopupContent;

    private final Label piePopupTitle = new Label();
    private final Label piePopupAmount = new Label();
    private final Label piePopupPercent = new Label();

    private final Label totalExpensesValue = new Label();
    private final Label topCategoryValue = new Label();
    private final Label categoryCountValue = new Label();

    private final ComboBox<String> periodFilter = new ComboBox<>();

    public AnalyticsView(TransactionService service, AppSettings settings, ExchangeRateService exchangeRateService) {
        this.service = service;
        this.settings = settings;
        this.exchangeRateService = exchangeRateService;


        setSpacing(15);
        setPadding(new Insets(20));

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Analytics",
                "Visualize your spending patterns"
        );

        periodFilter.getItems().addAll(
                "All Time",
                "Today",
                "Last 7 Days",
                "This Month",
                "Last Month",
                "This Year"
        );

        periodFilter.setValue("All Time");
        periodFilter.getStyleClass().add("analytics-period-filter");

        periodFilter.setOnAction(event -> refresh());

        HBox headerRow = new HBox(
                15,
                header,
                periodFilter
        );

        headerRow.setAlignment(Pos.CENTER);
        HBox.setHgrow(header, Priority.ALWAYS);

        Label totalExpensesTitle = new Label("TOTAL EXPENSES");
        totalExpensesTitle.getStyleClass().add("metric-title");

        totalExpensesValue.getStyleClass().addAll(
                "metric-value",
                "expense-value"
        );

        VBox totalExpensesCard = new VBox(
                8,
                totalExpensesTitle,
                totalExpensesValue
        );

        totalExpensesCard.getStyleClass().addAll(
                "content-card",
                "metric-card"
        );


        Label topCategoryTitle = new Label("TOP CATEGORY");
        topCategoryTitle.getStyleClass().add("metric-title");

        topCategoryValue.getStyleClass().add("metric-value");

        VBox topCategoryCard = new VBox(
                8,
                topCategoryTitle,
                topCategoryValue
        );

        topCategoryCard.getStyleClass().addAll(
                "content-card",
                "metric-card"
        );


        Label categoryCountTitle = new Label("AVERAGE EXPENSE");
        categoryCountTitle.getStyleClass().add("metric-title");

        categoryCountValue.getStyleClass().addAll(
                "metric-value",
                "balance-value"
        );

        VBox categoryCountCard = new VBox(
                8,
                categoryCountTitle,
                categoryCountValue
        );

        categoryCountCard.getStyleClass().addAll(
                "content-card",
                "metric-card"
        );

        HBox summaryRow = new HBox(
                15,
                totalExpensesCard,
                topCategoryCard,
                categoryCountCard
        );

        totalExpensesCard.setMaxWidth(Double.MAX_VALUE);
        topCategoryCard.setMaxWidth(Double.MAX_VALUE);
        categoryCountCard.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(totalExpensesCard, Priority.ALWAYS);
        HBox.setHgrow(topCategoryCard, Priority.ALWAYS);
        HBox.setHgrow(categoryCountCard, Priority.ALWAYS);

        expensePieChart = new PieChart();
        expensePieChart.setAnimated(false);
        expensePieChart.setLabelsVisible(false);
        expensePieChart.setTitle("Spending by Category");

        piePopupTitle.getStyleClass().add("analytics-tooltip-title");
        piePopupAmount.getStyleClass().add("analytics-tooltip-amount");
        piePopupPercent.getStyleClass().add("analytics-tooltip-percent");

        piePopupContent = new VBox(
                6,
                piePopupTitle,
                piePopupAmount,
                piePopupPercent
        );

        piePopupContent.getStyleClass().add("analytics-tooltip");
        piePopupContent.setMouseTransparent(true);

        piePopup.getContent().add(piePopupContent);

        piePopup.getScene().setFill(Color.TRANSPARENT);
        piePopup.getScene().getRoot().setStyle(
                "-fx-background-color: transparent;"
        );

        piePopup.setAutoFix(true);
        piePopup.setAutoHide(true);
        piePopup.setHideOnEscape(true);

        updatePiePopupTheme();

        settings.darkModeProperty().addListener(
                (observable, oldValue, newValue) -> updatePiePopupTheme()
        );

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        expenseBarChart = new BarChart<>(xAxis, yAxis);
        expenseBarChart.setAnimated(false);
        expenseBarChart.setTitle("Expenses by Category");

        expenseBarChart.getStyleClass().add("analytics-bar-chart");

        expenseBarChart.setLegendVisible(false);
        expenseBarChart.setVerticalGridLinesVisible(false);
        expenseBarChart.setHorizontalGridLinesVisible(true);

        expenseBarChart.setCategoryGap(18);
        expenseBarChart.setBarGap(4);

        expenseBarChart.setAlternativeColumnFillVisible(false);
        expenseBarChart.setAlternativeRowFillVisible(false);

        VBox pieCard = new VBox();
        pieCard.getStyleClass().add("content-card");
        pieCard.getChildren().add(expensePieChart);

        VBox barCard = new VBox();
        barCard.getStyleClass().add("content-card");
        barCard.getChildren().add(expenseBarChart);

        HBox chartsRow = new HBox(15, pieCard, barCard);

        pieCard.setMaxWidth(Double.MAX_VALUE);
        barCard.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(pieCard, Priority.ALWAYS);
        HBox.setHgrow(barCard, Priority.ALWAYS);

        getChildren().addAll(
                headerRow,
                summaryRow,
                chartsRow
        );

        settings.selectedCurrencyProperty().addListener(
                (observable, oldCurrency, newCurrency) -> refresh()
        );

        refresh();
    }
    public void refresh() {
        expensePieChart.getData().clear();
        expenseBarChart.getData().clear();

        String selectedCurrency = settings.selectedCurrencyProperty().get();

        NumberAxis yAxis = (NumberAxis) expenseBarChart.getYAxis();
        yAxis.setLabel("Amount (" + selectedCurrency + ")");

        Map<Category, Double> categoryData = new EnumMap<>(Category.class);

        int expenseTransactionCount = 0;

        for (Transaction transaction : service.getAllTransactions()) {

            if (transaction.getType() != TransactionType.EXPENSE) {
                continue;
            }

            if (!isTransactionInSelectedPeriod(transaction)) {
                continue;
            }

            if (transaction.getCategory() == null) {
                continue;
            }

            expenseTransactionCount++;

            double convertedAmount = exchangeRateService.convertToCurrency(
                    Math.abs(transaction.getAmount()),
                    transaction.getCurrency(),
                    selectedCurrency
            );

            categoryData.merge(
                    transaction.getCategory(),
                    convertedAmount,
                    Double::sum
            );
        }

        List<Map.Entry<Category, Double>> sortedCategoryData =
                categoryData.entrySet()
                        .stream()
                        .sorted(
                                Map.Entry.<Category, Double>comparingByValue(
                                        Comparator.reverseOrder()
                                )
                        )
                        .toList();

        double totalExpenses = sortedCategoryData.stream()
                .mapToDouble(Map.Entry::getValue)
                .sum();

        totalExpensesValue.setText(
                String.format(
                        "%,.2f %s",
                        totalExpenses,
                        selectedCurrency
                )
        );

        double averageExpense = expenseTransactionCount == 0
                ? 0
                : totalExpenses / expenseTransactionCount;

        categoryCountValue.setText(
                String.format(
                        "%,.2f %s",
                        averageExpense,
                        selectedCurrency
                )
        );

        if (sortedCategoryData.isEmpty()) {
            topCategoryValue.setText("No data");
        } else {
            topCategoryValue.setText(
                    sortedCategoryData.get(0)
                            .getKey()
                            .toString()
            );
        }

        for (Map.Entry<Category, Double> entry : sortedCategoryData) {
            PieChart.Data pieData = new PieChart.Data(
                    entry.getKey().toString(),
                    entry.getValue()
            );

            expensePieChart.getData().add(pieData);

            double percentage = entry.getValue() / totalExpenses * 100;

            pieData.nodeProperty().addListener(
                    (observable, oldNode, newNode) -> {

                        if (newNode == null) {
                            return;
                        }

                        addPieHoverEffect(newNode);

                        addPiePopup(
                                newNode,
                                entry.getKey().toString(),
                                entry.getValue(),
                                percentage,
                                selectedCurrency
                        );
                    }
            );

            if (pieData.getNode() != null) {
                addPieHoverEffect(pieData.getNode());

                addPiePopup(
                        pieData.getNode(),
                        entry.getKey().toString(),
                        entry.getValue(),
                        percentage,
                        selectedCurrency
                );
            }
        }

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");

        for (Map.Entry<Category, Double> entry : sortedCategoryData) {

            XYChart.Data<String, Number> barData =
                    new XYChart.Data<>(
                            entry.getKey().toString(),
                            entry.getValue()
                    );

            expenseSeries.getData().add(barData);

            double percentage =
                    totalExpenses == 0
                            ? 0
                            : entry.getValue() / totalExpenses * 100;

            barData.nodeProperty().addListener(
                    (observable, oldNode, newNode) -> {

                        if (newNode == null) {
                            return;
                        }

                        addPiePopup(
                                newNode,
                                entry.getKey().toString(),
                                entry.getValue(),
                                percentage,
                                selectedCurrency
                        );
                    }
            );

            if (barData.getNode() != null) {
                addPiePopup(
                        barData.getNode(),
                        entry.getKey().toString(),
                        entry.getValue(),
                        percentage,
                        selectedCurrency
                );
            }
        }

        expenseBarChart.getData().add(expenseSeries);
    }


    private void addPieHoverEffect(Node node) {

        node.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition =
                    new ScaleTransition(
                            Duration.millis(120),
                            node
                    );

            scaleTransition.setToX(1.03);
            scaleTransition.setToY(1.03);
            scaleTransition.play();
        });

        node.setOnMouseExited(event -> {
            ScaleTransition scaleTransition =
                    new ScaleTransition(
                            Duration.millis(120),
                            node
                    );

            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });
    }

    private void addPiePopup(
            Node node,
            String category,
            double amount,
            double percentage,
            String currency
    ) {
        node.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            piePopupTitle.setText(category);

            piePopupAmount.setText(
                    String.format("%,.2f %s", amount, currency)
            );

            piePopupPercent.setText(
                    String.format("%.1f%% of total expenses", percentage)
            );

            piePopup.show(
                    node,
                    event.getScreenX() + 18,
                    event.getScreenY() + 18
            );
        });

        node.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            if (piePopup.isShowing()) {
                piePopup.setX(event.getScreenX() + 18);
                piePopup.setY(event.getScreenY() + 18);
            }
        });

        node.addEventHandler(
                MouseEvent.MOUSE_EXITED,
                event -> piePopup.hide()
        );
    }

    private void updatePiePopupTheme() {
        piePopupContent.getStyleClass().removeAll(
                "analytics-tooltip-light",
                "analytics-tooltip-dark"
        );

        if (settings.darkModeProperty().get()) {
            piePopupContent.getStyleClass().add(
                    "analytics-tooltip-dark"
            );
        } else {
            piePopupContent.getStyleClass().add(
                    "analytics-tooltip-light"
            );
        }
    }

    private boolean isTransactionInSelectedPeriod(Transaction transaction) {
        LocalDate transactionDate = transaction.getDate();
        LocalDate today = LocalDate.now();

        return switch (periodFilter.getValue()) {
            case "Today" ->
                    transactionDate.equals(today);

            case "Last 7 Days" ->
                    !transactionDate.isBefore(today.minusDays(6))
                            && !transactionDate.isAfter(today);

            case "This Month" ->
                    transactionDate.getYear() == today.getYear()
                            && transactionDate.getMonth() == today.getMonth();

            case "Last Month" -> {
                LocalDate lastMonth = today.minusMonths(1);

                yield transactionDate.getYear() == lastMonth.getYear()
                        && transactionDate.getMonth() == lastMonth.getMonth();
            }

            case "This Year" ->
                    transactionDate.getYear() == today.getYear();

            default -> true;
        };
    }
}