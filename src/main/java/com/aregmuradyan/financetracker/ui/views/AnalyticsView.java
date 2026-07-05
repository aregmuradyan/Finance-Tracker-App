package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import java.util.Map;
import com.aregmuradyan.financetracker.model.Category;
import javafx.scene.layout.VBox;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;

public class AnalyticsView extends VBox {

    private final TransactionService service;
    private PieChart expensePieChart;
    private BarChart<String, Number> expenseBarChart;

    public AnalyticsView(TransactionService service) {
        this.service = service;

        setSpacing(15);
        setPadding(new Insets(20));

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Analytics",
                "Visualize your spending patterns"
        );

        expensePieChart = new PieChart();
        expensePieChart.setTitle("Spending by Category");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        expenseBarChart = new BarChart<>(xAxis, yAxis);

        expenseBarChart.setTitle("Expenses by Category");


        VBox pieCard = new VBox();
        pieCard.getStyleClass().add("content-card");
        pieCard.getChildren().add(expensePieChart);

        VBox barCard = new VBox();
        barCard.getStyleClass().add("content-card");
        barCard.getChildren().add(expenseBarChart);

        getChildren().addAll(
                header,
                pieCard,
                barCard
        );
        refresh();
    }
    public void refresh() {
        expensePieChart.getData().clear();
        expenseBarChart.getData().clear();

        Map<Category, Double> categoryData = service.getExpensesByCategory();

        for (Map.Entry<Category, Double> entry : categoryData.entrySet()) {
            expensePieChart.getData().add(
                    new PieChart.Data(
                            entry.getKey().toString(),
                            entry.getValue()
                    )
            );
        }

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");

        for (Map.Entry<Category, Double> entry : categoryData.entrySet()) {
            expenseSeries.getData().add(
                    new XYChart.Data<>(
                            entry.getKey().toString(),
                            entry.getValue()
                    )
            );
        }

        expenseBarChart.getData().add(expenseSeries);
    }
}