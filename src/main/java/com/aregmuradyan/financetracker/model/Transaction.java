package com.aregmuradyan.financetracker.model;

import java.time.LocalDate;

public class Transaction {
    private long id;
    private String name;
    private String description;
    private double amount;
    private String currency;
    private TransactionType type;
    private Category category;
    private LocalDate date;

    public Transaction(long id, String name, String description, double amount,
                       String currency, TransactionType type, Category category, LocalDate date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }


    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }


    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCurrency() {
        return currency;
    }


    public void setType(TransactionType type) {
        this.type = type;
    }
    public TransactionType getType() {
        return type;
    }


    public void setCategory(Category category) {
        this.category = category;
    }
    public Category getCategory() {
        return category;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalDate getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", type=" + type +
                ", category=" + category +
                ", date=" + date +
                '}';
    }
}