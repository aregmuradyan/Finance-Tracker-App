package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import java.util.Map;
import com.aregmuradyan.financetracker.model.Category;
import javafx.scene.layout.VBox;

public class AnalyticsView extends VBox {

    private final TransactionService service;

    public AnalyticsView(TransactionService service) {
        this.service = service;

        setSpacing(15);
        setPadding(new Insets(20));

        Label title = new Label("Analytics");

        Map<Category, Double> categoryData =
                service.getExpensesByCategory();

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Spending by Category");

        for (Map.Entry<Category, Double> entry : categoryData.entrySet()) {
            pieChart.getData().add(
                    new PieChart.Data(
                            entry.getKey().toString(),
                            entry.getValue()
                    )
            );
        }

        getChildren().addAll(
                title,
                pieChart
        );
    }
}