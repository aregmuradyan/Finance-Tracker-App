package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.Category;
import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.model.TransactionType;
import com.aregmuradyan.financetracker.service.AppSettings;
import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Comparator;

public class TransactionsView extends VBox {

    private final TransactionService service;
    private final ObservableList<Transaction> transactionList;
    private final AppSettings settings;

    private long nextId;

    public TransactionsView(TransactionService service, AppSettings settings) {
        this.service = service;
        this.settings = settings;
        this.transactionList = FXCollections.observableArrayList(this.service.getAllTransactions());
        this.nextId = calculateNextId();
        sortTransactionsByNewestId();

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
        addCurrencyOptions(currencyBox);
        currencyBox.setValue(settings.getSelectedCurrency());
        currencyBox.setVisibleRowCount(6);

        settings.selectedCurrencyProperty().addListener((obs, oldCurrency, newCurrency) -> {
            currencyBox.setValue(newCurrency);
        });

        ComboBox<TransactionType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TransactionType.values());
        typeBox.setValue(TransactionType.EXPENSE);
        typeBox.setVisibleRowCount(4);

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Category.values());
        categoryBox.setValue(Category.OTHER);
        categoryBox.setVisibleRowCount(8);

        DatePicker datePicker = new DatePicker(LocalDate.now());

        nameField.setMaxWidth(Double.MAX_VALUE);
        descriptionField.setMaxWidth(Double.MAX_VALUE);
        amountField.setMaxWidth(Double.MAX_VALUE);
        currencyBox.setMaxWidth(Double.MAX_VALUE);
        typeBox.setMaxWidth(Double.MAX_VALUE);
        categoryBox.setMaxWidth(Double.MAX_VALUE);
        datePicker.setMaxWidth(Double.MAX_VALUE);

        Button addButton = new Button("Add Transaction");
        addButton.getStyleClass().add("action-button");
        addButton.getStyleClass().add("primary-action-button");

        Button removeButton = new Button("Remove Selected");
        removeButton.getStyleClass().add("action-button");
        removeButton.getStyleClass().add("danger-action-button");

        Button editButton = new Button("Edit Selected");
        editButton.getStyleClass().add("action-button");
        editButton.getStyleClass().add("secondary-action-button");

        TableView<Transaction> table = new TableView<>(transactionList);
        table.getStyleClass().add("clean-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(42);
        table.setPrefHeight(320);
        table.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(table, Priority.ALWAYS);

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

        idColumn.getStyleClass().add("id-column");
        nameColumn.getStyleClass().add("name-column");
        amountColumn.getStyleClass().add("amount-column");
        currencyColumn.getStyleClass().add("currency-column");
        typeColumn.getStyleClass().add("type-column");
        categoryColumn.getStyleClass().add("category-column");
        dateColumn.getStyleClass().add("date-column");

        idColumn.setPrefWidth(70);
        nameColumn.setPrefWidth(170);
        amountColumn.setPrefWidth(140);
        currencyColumn.setPrefWidth(115);
        typeColumn.setPrefWidth(120);
        categoryColumn.setPrefWidth(170);
        dateColumn.setPrefWidth(140);

        idColumn.setReorderable(false);
        nameColumn.setReorderable(false);
        amountColumn.setReorderable(false);
        currencyColumn.setReorderable(false);
        typeColumn.setReorderable(false);
        categoryColumn.setReorderable(false);
        dateColumn.setReorderable(false);

        idColumn.setSortable(false);
        nameColumn.setSortable(false);
        amountColumn.setSortable(false);
        currencyColumn.setSortable(false);
        typeColumn.setSortable(false);
        categoryColumn.setSortable(false);
        dateColumn.setSortable(false);

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
            sortTransactionsByNewestId();

            nameField.clear();
            descriptionField.clear();
            amountField.clear();
            currencyBox.setValue(settings.getSelectedCurrency());
            typeBox.setValue(TransactionType.EXPENSE);
            categoryBox.setValue(Category.OTHER);
            datePicker.setValue(LocalDate.now());
        });

        editButton.setOnAction(e -> {
            Transaction selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No transaction selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a transaction to edit.");
                alert.showAndWait();
                return;
            }

            openEditDialog(selected, table);
        });

        removeButton.setOnAction(e -> {
            Transaction selected = table.getSelectionModel().getSelectedItem();

            if (selected != null) {
                this.service.removeTransaction(selected);
                transactionList.remove(selected);
            }
        });

        Label formTitle = new Label("Add Transaction");
        formTitle.getStyleClass().add("section-title");

        GridPane formGrid = new GridPane();
        formGrid.getStyleClass().add("transaction-form-grid");
        formGrid.setHgap(12);
        formGrid.setVgap(12);

        ColumnConstraints leftColumn = new ColumnConstraints();
        leftColumn.setPercentWidth(50);

        ColumnConstraints rightColumn = new ColumnConstraints();
        rightColumn.setPercentWidth(50);

        formGrid.getColumnConstraints().addAll(leftColumn, rightColumn);

        formGrid.add(nameField, 0, 0);
        formGrid.add(amountField, 1, 0);

        formGrid.add(descriptionField, 0, 1);
        formGrid.add(datePicker, 1, 1);

        formGrid.add(currencyBox, 0, 2);
        formGrid.add(typeBox, 1, 2);

        formGrid.add(categoryBox, 0, 3, 2, 1);

        HBox addButtonRow = new HBox(addButton);
        addButtonRow.getStyleClass().add("form-button-row");
        addButtonRow.setAlignment(Pos.CENTER_RIGHT);

        VBox formCard = new VBox();
        formCard.getStyleClass().add("content-card");
        formCard.getStyleClass().add("transaction-form-card");

        formCard.getChildren().addAll(
                formTitle,
                formGrid,
                addButtonRow
        );

        Label tableTitle = new Label("Transaction History");
        tableTitle.getStyleClass().add("section-title");

        Region tableSpacer = new Region();
        HBox.setHgrow(tableSpacer, Priority.ALWAYS);

        HBox tableActions = new HBox();
        tableActions.getStyleClass().add("table-actions");
        tableActions.setAlignment(Pos.CENTER_RIGHT);
        tableActions.getChildren().addAll(
                editButton,
                removeButton
        );

        HBox tableHeader = new HBox();
        tableHeader.getStyleClass().add("table-header-row");
        tableHeader.setAlignment(Pos.CENTER_LEFT);
        tableHeader.getChildren().addAll(
                tableTitle,
                tableSpacer,
                tableActions
        );

        VBox tableCard = new VBox();
        tableCard.getStyleClass().add("content-card");
        tableCard.getStyleClass().add("table-card");

        tableCard.getChildren().addAll(
                tableHeader,
                table
        );

        getChildren().addAll(
                header,
                formCard,
                tableCard
        );
    }
    private void sortTransactionsByNewestId() {
        FXCollections.sort(
                transactionList,
                Comparator.comparingLong(Transaction::getId).reversed()
        );
    }
    private long calculateNextId() {
        long maxId = 0;

        for (Transaction transaction : service.getAllTransactions()) {
            if (transaction.getId() > maxId) {
                maxId = transaction.getId();
            }
        }

        return maxId + 1;
    }

    private void addCurrencyOptions(ComboBox<String> currencyBox) {
        currencyBox.getItems().addAll(
                "USD",
                "AMD",
                "EUR",
                "RUB",
                "GBP",
                "CHF",
                "CAD",
                "AUD",
                "JPY",
                "CNY"
        );
    }

    private void openEditDialog(Transaction transaction, TableView<Transaction> table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");
        dialog.setHeaderText("Update transaction details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(transaction.getName());
        TextField descriptionField = new TextField(transaction.getDescription());
        TextField amountField = new TextField(String.valueOf(transaction.getAmount()));

        ComboBox<String> currencyBox = new ComboBox<>();
        addCurrencyOptions(currencyBox);

        if (!currencyBox.getItems().contains(transaction.getCurrency())) {
            currencyBox.getItems().add(transaction.getCurrency());
        }

        currencyBox.setValue(transaction.getCurrency());
        currencyBox.setVisibleRowCount(6);

        ComboBox<TransactionType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(TransactionType.values());
        typeBox.setValue(transaction.getType());
        typeBox.setVisibleRowCount(4);

        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Category.values());
        categoryBox.setValue(transaction.getCategory());
        categoryBox.setVisibleRowCount(8);

        DatePicker datePicker = new DatePicker(transaction.getDate());

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        grid.addRow(0, new Label("Name"), nameField);
        grid.addRow(1, new Label("Description"), descriptionField);
        grid.addRow(2, new Label("Amount"), amountField);
        grid.addRow(3, new Label("Currency"), currencyBox);
        grid.addRow(4, new Label("Type"), typeBox);
        grid.addRow(5, new Label("Category"), categoryBox);
        grid.addRow(6, new Label("Date"), datePicker);

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait().ifPresent(result -> {
            if (result == saveButtonType) {
                double amount = Double.parseDouble(amountField.getText());

                transaction.setName(nameField.getText());
                transaction.setDescription(descriptionField.getText());
                transaction.setAmount(amount);
                transaction.setCurrency(currencyBox.getValue());
                transaction.setType(typeBox.getValue());
                transaction.setCategory(categoryBox.getValue());
                transaction.setDate(datePicker.getValue());

                service.saveTransactions();
                table.refresh();
            }
        });
    }
}