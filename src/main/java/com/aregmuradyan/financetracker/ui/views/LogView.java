package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.LogEntry;
import com.aregmuradyan.financetracker.service.LogService;
import com.aregmuradyan.financetracker.ui.helper.PageHeader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class LogView extends VBox {

    private final LogService service;

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
        logInput.setPrefRowCount(4);

        Button addButton = new Button("Add Log");
        addButton.getStyleClass().add("action-button");
        addButton.getStyleClass().add("primary-action-button");

        Button removeButton = new Button("Remove Selected");
        removeButton.getStyleClass().add("action-button");
        removeButton.getStyleClass().add("primary-action-button");

        ListView<LogEntry> logList = new ListView<>();
        logList.getItems().addAll(service.getAllLogs());

        addButton.setOnAction(e -> {
            String text = logInput.getText().trim();

            if (!text.isEmpty()) {
                LogEntry log = new LogEntry(text, LocalDateTime.now());

                service.addLog(log);
                logList.getItems().add(log);

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

        VBox inputCard = new VBox();
        inputCard.getStyleClass().add("content-card");

        inputCard.getChildren().addAll(
                logInput,
                addButton
        );

        VBox logsCard = new VBox();
        logsCard.getStyleClass().add("content-card");

        logsCard.getChildren().addAll(
                removeButton,
                logList
        );

        getChildren().addAll(
                header,
                inputCard,
                logsCard
        );
    }
}