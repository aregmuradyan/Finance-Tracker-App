package com.aregmuradyan.financetracker.service;

import com.aregmuradyan.financetracker.model.LogEntry;
import com.aregmuradyan.financetracker.repository.LogRepository;

import java.util.List;

public class LogService {

    private final LogRepository repository;

    public LogService(LogRepository repository) {
        this.repository = repository;
    }

    public void addLog(LogEntry log) {
        repository.addLog(log);
    }

    public void removeLog(LogEntry log) {
        repository.removeLog(log);
    }

    public List<LogEntry> getAllLogs() {
        return repository.getAllLogs();
    }
}