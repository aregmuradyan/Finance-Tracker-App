package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.LogEntry;
import com.aregmuradyan.financetracker.service.LogService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Comparator;

public class LogView extends VBox {

    private final LogService service;
    private static final DateTimeFormatter LOG_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    public LogView(LogService service) {
        this.service = service;

        setSpacing(15);
        setPadding(new Insets(20));

        getStyleClass().add("page");
        PageHeader header = new PageHeader(
                "Personal Log",
                "Keep notes about your financial journey"
        );

        TextArea logInput = new TextArea();
        logInput.setPromptText("Write a note about your spending, goals, or thoughts...");
        logInput.setWrapText(true);
        logInput.getStyleClass().add("log-text-area");

        Button addButton = new Button("Add Log");
        addButton.getStyleClass().add("action-button");
        addButton.getStyleClass().add("primary-action-button");

        Button removeButton = new Button("Remove Selected");
        removeButton.getStyleClass().add("action-button");
        removeButton.getStyleClass().add("danger-action-button");

        ObservableList<LogEntry> logItems = FXCollections.observableArrayList(service.getAllLogs());

        FXCollections.sort(
                logItems,
                Comparator.comparing(LogEntry::getTimestamp).reversed()
        );

        ListView<LogEntry> logList = new ListView<>(logItems);
        logList.getStyleClass().add("log-list");
        logList.setPrefHeight(360);
        logList.setPlaceholder(new Label("No notes yet. Add your first financial reflection."));
        logList.setCellFactory(listView -> createLogCell());

        addButton.setOnAction(e -> {
            String text = logInput.getText().trim();

            if (!text.isEmpty()) {
                LogEntry log = new LogEntry(text, LocalDateTime.now());

                service.addLog(log);
                logList.getItems().add(0, log);
                logList.getSelectionModel().select(log);
                logInput.clear();
            }
        });

        removeButton.setOnAction(e -> {
            LogEntry selected = logList.getSelectionModel().getSelectedItem();

            if (selected != null) {
                service.removeLog(selected);
                logList.getItems().remove(selected);
            }
        });

        Label inputTitle = new Label("New Note");
        inputTitle.getStyleClass().add("section-title");


        HBox addButtonRow = new HBox(addButton);
        addButtonRow.getStyleClass().add("log-button-row");
        addButtonRow.setAlignment(Pos.CENTER_RIGHT);

        VBox inputCard = new VBox();
        inputCard.getStyleClass().add("content-card");
        inputCard.getStyleClass().add("log-input-card");

        inputCard.getChildren().addAll(
                inputTitle,
                logInput,
                addButtonRow
        );

        Label logsTitle = new Label("Log History");
        logsTitle.getStyleClass().add("section-title");

        Region logsSpacer = new Region();
        HBox.setHgrow(logsSpacer, Priority.ALWAYS);

        HBox logsHeader = new HBox();
        logsHeader.getStyleClass().add("log-header-row");
        logsHeader.setAlignment(Pos.CENTER_LEFT);
        logsHeader.getChildren().addAll(
                logsTitle,
                logsSpacer,
                removeButton
        );

        VBox logsCard = new VBox();
        logsCard.getStyleClass().add("content-card");
        logsCard.getStyleClass().add("log-history-card");

        logsCard.getChildren().addAll(
                logsHeader,
                logList
        );

        getChildren().addAll(
                header,
                inputCard,
                logsCard
        );
    }

    private ListCell<LogEntry> createLogCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(LogEntry item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Label textLabel = new Label(item.getText());
                textLabel.getStyleClass().add("log-entry-text");
                textLabel.setWrapText(true);

                Label dateLabel = new Label(item.getTimestamp().format(LOG_DATE_FORMAT));
                dateLabel.getStyleClass().add("log-entry-date");

                VBox logEntryCard = new VBox();
                logEntryCard.getStyleClass().add("log-entry-card");
                logEntryCard.getChildren().addAll(
                        textLabel,
                        dateLabel
                );

                setText(null);
                setGraphic(logEntryCard);
            }
        };
    }
}