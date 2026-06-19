package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.service.TransactionService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class DashboardView extends VBox {

    private final TransactionService service;

    public DashboardView(TransactionService service) {
        this.service = service;

        setSpacing(15);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Dashboard");

        Label balanceLabel = new Label("Balance: " + service.getBalance() + " AMD");
        Label incomeLabel = new Label("Income: " + service.getTotalIncome() + " AMD");
        Label expensesLabel = new Label("Expenses: " + service.getTotalExpenses() + " AMD");

        Label recentTitle = new Label("Recent Transactions");

        VBox recentTransactionsBox = new VBox();
        recentTransactionsBox.setSpacing(5);

        List<Transaction> transactions = service.getAllTransactions();

        int startIndex = Math.max(0, transactions.size() - 5);

        for (int i = startIndex; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);

            Label transactionLabel = new Label(
                    transaction.getName() + " | " +
                            transaction.getAmount() + " " +
                            transaction.getCurrency() + " | " +
                            transaction.getType()
            );

            recentTransactionsBox.getChildren().add(transactionLabel);
        }

        getChildren().addAll(
                titleLabel,
                balanceLabel,
                incomeLabel,
                expensesLabel,
                recentTitle,
                recentTransactionsBox
        );
    }
}