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

    public AnalyticsView(TransactionService service) {
        this.service = service;

        setSpacing(15);
        setPadding(new Insets(20));

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Analytics",
                "Visualize your spending patterns"
        );

        Map<Category, Double> categoryData =
                service.getExpensesByCategory();

        PieChart expensePieChart = new PieChart();
        expensePieChart.setTitle("Spending by Category");

        for (Map.Entry<Category, Double> entry : categoryData.entrySet()) {
            expensePieChart.getData().add(
                    new PieChart.Data(
                            entry.getKey().toString(),
                            entry.getValue()
                    )
            );
        }
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> expenseBarChart =
                new BarChart<>(xAxis, yAxis);

        expenseBarChart.setTitle("Expenses by Category");

        XYChart.Series<String, Number> expenseSeries =
                new XYChart.Series<>();

        expenseSeries.setName("Expenses");

        for (Map.Entry<Category, Double> entry :
                service.getExpensesByCategory().entrySet()) {

            expenseSeries.getData().add(
                    new XYChart.Data<>(
                            entry.getKey().toString(),
                            entry.getValue()
                    )
            );
        }

        expenseBarChart.getData().add(expenseSeries);
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
    }
}