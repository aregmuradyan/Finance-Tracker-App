package com.aregmuradyan.financetracker.service;

import com.aregmuradyan.financetracker.model.TransactionType;
import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.repository.TransactionRepository;

import java.util.List;

public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public void addTransaction(Transaction transaction) {
        repository.addTransaction(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return repository.getAllTransactions();
    }

    public double getTotalIncome() {
        double total = 0;

        for (Transaction transaction : repository.getAllTransactions()) {
            if (transaction.getType() == TransactionType.INCOME) {
                total += transaction.getAmount();
            }
        }

        return total;
    }

    public double getTotalExpenses() {
        double total = 0;

        for (Transaction transaction : repository.getAllTransactions()) {
            if (transaction.getType() == TransactionType.EXPENSE) {
                total += transaction.getAmount();
            }
        }

        return total;
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

}