package com.aregmuradyan.financetracker.model;

import java.time.LocalDateTime;

public class LogEntry {
    private String text;
    private LocalDateTime timestamp;

    public LogEntry() {
    }

    public LogEntry(String text, LocalDateTime timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return timestamp + " — " + text;
    }
}