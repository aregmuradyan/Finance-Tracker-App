package com.aregmuradyan.financetracker.repository;

import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.storage.TransactionStorage;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();
    private final TransactionStorage storage;

    public TransactionRepository() {
        storage = new TransactionStorage();
        transactions.addAll(storage.loadTransactions());
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        storage.saveTransactions(transactions);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        storage.saveTransactions(transactions);
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public void saveTransactions() {
        storage.saveTransactions(transactions);
    }
}