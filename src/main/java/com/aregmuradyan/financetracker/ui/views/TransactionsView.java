package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.Category;
import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.model.TransactionType;
import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class TransactionsView extends VBox {

    private final TransactionService service;
    private final ObservableList<Transaction> transactionList;

    private long nextId = 1;

    public TransactionsView(TransactionService service) {
        this.service = service;
        this.transactionList = FXCollections.observableArrayList(this.service.getAllTransactions());

        setSpacing(15);
        setPadding(new Insets(20));

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Transactions",
                "Manage your income and expenses"
        );
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        ComboBox<String> currencyBox = new ComboBox<>();
        currencyBox.getItems().addAll("AMD", "USD", "EUR", "RUB");
        currencyBox.setValue("AMD");

        ComboBox<TransactionType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TransactionType.values());
        typeBox.setValue(TransactionType.EXPENSE);

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Category.values());
        categoryBox.setValue(Category.OTHER);

        DatePicker datePicker = new DatePicker(LocalDate.now());

        Button addButton = new Button("Add Transaction");
        addButton.getStyleClass().add("action-button");
        Button removeButton = new Button("Remove Selected");
        removeButton.getStyleClass().add("action-button");

        TableView<Transaction> table = new TableView<>(transactionList);

        TableColumn<Transaction, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, String> currencyColumn = new TableColumn<>("Currency");
        currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));

        TableColumn<Transaction, TransactionType> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Category> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                amountColumn,
                currencyColumn,
                typeColumn,
                categoryColumn,
                dateColumn
        );

        addButton.setOnAction(e -> {
            double amount = Double.parseDouble(amountField.getText());

            Transaction transaction = new Transaction(
                    nextId++,
                    nameField.getText(),
                    descriptionField.getText(),
                    amount,
                    currencyBox.getValue(),
                    typeBox.getValue(),
                    categoryBox.getValue(),
                    datePicker.getValue()
            );

            this.service.addTransaction(transaction);
            transactionList.add(transaction);

            nameField.clear();
            descriptionField.clear();
            amountField.clear();
            currencyBox.setValue("AMD");
            typeBox.setValue(TransactionType.EXPENSE);
            categoryBox.setValue(Category.OTHER);
            datePicker.setValue(LocalDate.now());
        });

        removeButton.setOnAction(e -> {
            Transaction selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                this.service.removeTransaction(selected);
                transactionList.remove(selected);
            }
        });


        VBox formCard = new VBox();
        formCard.getStyleClass().add("content-card");

        formCard.getChildren().addAll(
                nameField,
                descriptionField,
                amountField,
                currencyBox,
                typeBox,
                categoryBox,
                datePicker,
                addButton
        );

        VBox tableCard = new VBox();
        tableCard.getStyleClass().add("content-card");

        tableCard.getChildren().addAll(
                removeButton,
                table
        );

        getChildren().addAll(
                header,
                formCard,
                tableCard
        );
    }
}