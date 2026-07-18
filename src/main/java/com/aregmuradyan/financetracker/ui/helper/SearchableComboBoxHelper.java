package com.aregmuradyan.financetracker.ui.helper;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

public class SearchableComboBoxHelper {

    public static void makeSearchable(ComboBox<String> comboBox) {
        ObservableList<String> originalItems = FXCollections.observableArrayList(comboBox.getItems());
        FilteredList<String> filteredItems = new FilteredList<>(originalItems, item -> true);

        final boolean[] internalChange = {false};
        final String[] lastValidValue = {comboBox.getValue()};

        comboBox.setItems(filteredItems);
        comboBox.setEditable(true);

        comboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && originalItems.contains(newValue)) {
                lastValidValue[0] = newValue;
            }
        });

        comboBox.getEditor().setOnMouseClicked(e -> {
            if (!comboBox.isShowing()) {
                filteredItems.setPredicate(item -> true);
                comboBox.show();
                comboBox.getEditor().selectAll();
            }
        });

        comboBox.setOnShowing(e -> {
            internalChange[0] = true;

            filteredItems.setPredicate(item -> true);

            if (lastValidValue[0] != null) {
                comboBox.getEditor().setText(lastValidValue[0]);
            }

            comboBox.getEditor().selectAll();

            internalChange[0] = false;
        });

        comboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (internalChange[0]) {
                return;
            }

            if (!comboBox.isShowing()) {
                return;
            }

            String searchText = newText == null ? "" : newText.toUpperCase().trim();

            Platform.runLater(() -> {
                if (!comboBox.isShowing()) {
                    return;
                }

                filteredItems.setPredicate(item ->
                        item.toUpperCase().contains(searchText)
                );
            });
        });

        comboBox.setOnHidden(e -> {
            restoreValue(comboBox, filteredItems, lastValidValue, internalChange);
        });

        comboBox.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                restoreValue(comboBox, filteredItems, lastValidValue, internalChange);
            }
        });
    }

    private static void restoreValue(
            ComboBox<String> comboBox,
            FilteredList<String> filteredItems,
            String[] lastValidValue,
            boolean[] internalChange
    ) {
        Platform.runLater(() -> {
            internalChange[0] = true;

            filteredItems.setPredicate(item -> true);

            if (lastValidValue[0] != null) {
                comboBox.setValue(lastValidValue[0]);
                comboBox.getEditor().setText(lastValidValue[0]);
            }

            internalChange[0] = false;
        });
    }
}