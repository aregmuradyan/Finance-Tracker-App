package com.aregmuradyan.financetracker.repository;

import com.aregmuradyan.financetracker.model.LogEntry;
import com.aregmuradyan.financetracker.storage.LogStorage;

import java.util.ArrayList;
import java.util.List;

public class LogRepository {

    private final List<LogEntry> logs = new ArrayList<>();
    private final LogStorage storage;

    public LogRepository() {
        storage = new LogStorage();
        logs.addAll(storage.loadLogs());
    }

    public void addLog(LogEntry log) {
        logs.add(log);
        storage.saveLogs(logs);
    }

    public void removeLog(LogEntry log) {
        logs.remove(log);
        storage.saveLogs(logs);
    }

    public List<LogEntry> getAllLogs() {
        return logs;
    }
}