package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import com.aregmuradyan.financetracker.service.AppSettings;
import com.aregmuradyan.financetracker.service.ExchangeRateService;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;

public class DashboardView extends VBox {

    private final TransactionService service;
    private final AppSettings settings;
    private final ExchangeRateService exchangeRateService;
    private Label balanceLabel;
    private Label incomeLabel;
    private Label expensesLabel;
    private VBox recentTransactionsBox;

    public DashboardView(TransactionService service, AppSettings settings, ExchangeRateService exchangeRateService) {
        this.service = service;
        this.settings = settings;
        this.exchangeRateService = exchangeRateService;

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Dashboard",
                "Your financial overview"
        );

        balanceLabel = new Label();
        incomeLabel = new Label();
        expensesLabel = new Label();

        VBox balanceCard = createMetricCard("Balance", balanceLabel, "balance-value");
        VBox incomeCard = createMetricCard("Income", incomeLabel, "income-value");
        VBox expensesCard = createMetricCard("Expenses", expensesLabel, "expense-value");

        HBox.setHgrow(balanceCard, Priority.ALWAYS);
        HBox.setHgrow(incomeCard, Priority.ALWAYS);
        HBox.setHgrow(expensesCard, Priority.ALWAYS);

        HBox summaryRow = new HBox(
                balanceCard,
                incomeCard,
                expensesCard
        );
        summaryRow.getStyleClass().add("dashboard-summary-row");

        Label recentTitle = new Label("Recent Transactions");
        recentTitle.getStyleClass().add("section-title");

        recentTransactionsBox = new VBox();
        recentTransactionsBox.getStyleClass().add("recent-list");

        VBox recentCard = new VBox();
        recentCard.getStyleClass().add("content-card");
        recentCard.getChildren().addAll(
                recentTitle,
                recentTransactionsBox
        );

        getChildren().addAll(
                header,
                summaryRow,
                recentCard
        );

        refresh();
    }
    public void refresh() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Transaction transaction : service.getAllTransactions()) {
            double convertedAmount = convertToSelectedCurrency(transaction);

            if (transaction.getType().toString().equalsIgnoreCase("INCOME")) {
                totalIncome += convertedAmount;
            } else if (transaction.getType().toString().equalsIgnoreCase("EXPENSE")) {
                totalExpenses += convertedAmount;
            }
        }

        double balance = totalIncome - totalExpenses;

        balanceLabel.setText(formatMoney(balance));
        incomeLabel.setText(formatMoney(totalIncome));
        expensesLabel.setText(formatMoney(totalExpenses));

        recentTransactionsBox.getChildren().clear();

        List<Transaction> transactions = service.getAllTransactions();

        if (transactions.isEmpty()) {
            Label emptyLabel = new Label("No transactions yet.");
            emptyLabel.getStyleClass().add("empty-state");
            recentTransactionsBox.getChildren().add(emptyLabel);
            return;
        }

        int startIndex = Math.max(0, transactions.size() - 5);

        for (int i = transactions.size() - 1; i >= startIndex; i--) {
            Transaction transaction = transactions.get(i);
            recentTransactionsBox.getChildren().add(
                    createRecentTransactionRow(transaction)
            );
        }
    }

    private VBox createMetricCard(String title, Label valueLabel, String valueClass) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("metric-title");

        valueLabel.getStyleClass().add("metric-value");
        valueLabel.getStyleClass().add(valueClass);

        VBox card = new VBox();
        card.getStyleClass().add("content-card");
        card.getStyleClass().add("metric-card");

        card.getChildren().addAll(
                titleLabel,
                valueLabel
        );

        return card;
    }

    private HBox createRecentTransactionRow(Transaction transaction) {
        Label nameLabel = new Label(transaction.getName());
        nameLabel.getStyleClass().add("recent-name");

        Label metaLabel = new Label(
                transaction.getDate() + " • " + transaction.getType()
        );
        metaLabel.getStyleClass().add("recent-meta");

        VBox textBox = new VBox();
        textBox.getStyleClass().add("recent-text-box");
        textBox.getChildren().addAll(nameLabel, metaLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label amountLabel = new Label(
                formatMoney(convertToSelectedCurrency(transaction))
        );
        amountLabel.getStyleClass().add("recent-amount");

        if (transaction.getType().toString().equals("INCOME")) {
            amountLabel.getStyleClass().add("income-value");
        } else {
            amountLabel.getStyleClass().add("expense-value");
        }

        HBox row = new HBox();
        row.getStyleClass().add("recent-row");
        row.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(
                textBox,
                spacer,
                amountLabel
        );

        return row;
    }

    private double convertToSelectedCurrency(Transaction transaction) {
        String fromCurrency = transaction.getCurrency();
        String toCurrency = settings.getSelectedCurrency();

        if (fromCurrency.equals(toCurrency)) {
            return transaction.getAmount();
        }

        return exchangeRateService.convert(
                transaction.getAmount(),
                fromCurrency,
                toCurrency
        );
    }

    private String formatMoney(double amount) {
        return String.format("%,.2f %s", amount, settings.getSelectedCurrency());
    }
}