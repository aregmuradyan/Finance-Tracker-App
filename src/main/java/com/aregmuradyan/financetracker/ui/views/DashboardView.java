package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class DashboardView extends VBox {

    private final TransactionService service;
    private Label balanceLabel;
    private Label incomeLabel;
    private Label expensesLabel;
    private VBox recentTransactionsBox;

    public DashboardView(TransactionService service) {
        this.service = service;

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Dashboard",
                "Your financial overview"
        );

        balanceLabel = new Label();
        incomeLabel = new Label();
        expensesLabel = new Label();

        Label recentTitle = new Label("Recent Transactions");

        recentTransactionsBox = new VBox();
        recentTransactionsBox.setSpacing(5);



        VBox summaryCard = new VBox();
        summaryCard.getStyleClass().add("content-card");

        summaryCard.getChildren().addAll(
                balanceLabel,
                incomeLabel,
                expensesLabel
        );

        VBox recentCard = new VBox();
        recentCard.getStyleClass().add("content-card");

        recentCard.getChildren().addAll(
                recentTitle,
                recentTransactionsBox
        );

        getChildren().addAll(
                header,
                summaryCard,
                recentCard
        );
        refresh();
    }
    public void refresh() {
        balanceLabel.setText("Balance: " + service.getBalance() + " AMD");
        incomeLabel.setText("Income: " + service.getTotalIncome() + " AMD");
        expensesLabel.setText("Expenses: " + service.getTotalExpenses() + " AMD");

        recentTransactionsBox.getChildren().clear();

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
    }
}