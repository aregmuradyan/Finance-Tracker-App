package com.aregmuradyan.financetracker.ui.views;

import com.aregmuradyan.financetracker.model.LogEntry;
import com.aregmuradyan.financetracker.service.LogService;
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

        Label title = new Label("Personal Log");

        TextArea logInput = new TextArea();
        logInput.setPromptText("Write a note about your spending, goals, or thoughts...");
        logInput.setPrefRowCount(4);

        Button addButton = new Button("Add Log");
        addButton.getStyleClass().add("action-button");

        Button removeButton = new Button("Remove Selected");
        removeButton.getStyleClass().add("action-button");

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

        getChildren().addAll(
                title,
                logInput,
                addButton,
                removeButton,
                logList
        );
    }
}