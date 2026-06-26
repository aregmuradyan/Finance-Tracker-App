package com.aregmuradyan.financetracker.storage;

import com.aregmuradyan.financetracker.model.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LogStorage {

    private static final String FILE_NAME = "logs.json";

    private final ObjectMapper objectMapper;

    public LogStorage() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void saveLogs(List<LogEntry> logs) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_NAME), logs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<LogEntry> loadLogs() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return List.of();
        }

        try {
            LogEntry[] logs =
                    objectMapper.readValue(file, LogEntry[].class);

            return Arrays.asList(logs);

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}