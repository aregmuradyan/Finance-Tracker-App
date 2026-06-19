package com.aregmuradyan.financetracker.service;

import com.aregmuradyan.financetracker.model.Category;
import com.aregmuradyan.financetracker.model.TransactionType;
import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    public void removeTransaction(Transaction transaction) {
        repository.removeTransaction(transaction);
    }

    public List<Transaction> getTransactions() {
        return repository.getAllTransactions();
    }

    public Map<Category, Double> getExpensesByCategory() {

        Map<Category, Double> expensesByCategory = new HashMap<>();

        for (Transaction transaction : repository.getAllTransactions()) {

            if (transaction.getType() == TransactionType.EXPENSE) {

                Category category = transaction.getCategory();

                double currentAmount =
                        expensesByCategory.getOrDefault(category, 0.0);

                expensesByCategory.put(
                        category,
                        currentAmount + transaction.getAmount()
                );
            }
        }

        return expensesByCategory;
    }
    public List<Transaction> getExpenseTransactions() {

        List<Transaction> expenses = new ArrayList<>();

        for (Transaction transaction : repository.getAllTransactions()) {

            if (transaction.getType() == TransactionType.EXPENSE) {
                expenses.add(transaction);
            }
        }

        return expenses;
    }
    public List<Transaction> getIncomeTransactions() {

        List<Transaction> income = new ArrayList<>();

        for (Transaction transaction : repository.getAllTransactions()) {

            if (transaction.getType() == TransactionType.INCOME) {
                income.add(transaction);
            }
        }

        return income;
    }
}