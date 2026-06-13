package com.aregmuradyan.financetracker;

import com.aregmuradyan.financetracker.model.Category;
import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.model.TransactionType;
import com.aregmuradyan.financetracker.repository.TransactionRepository;
import com.aregmuradyan.financetracker.service.TransactionService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        TransactionRepository repository = new TransactionRepository();
        TransactionService service = new TransactionService(repository);

        Transaction salary = new Transaction(
                1,
                "June Salary",
                "Monthly salary",
                500000,
                "AMD",
                TransactionType.INCOME,
                Category.SALARY,
                LocalDate.now()
        );
        Transaction expenses = new Transaction(
                1,
                "June Salary",
                "Monthly salary",
                480000,
                "AMD",
                TransactionType.EXPENSE,
                Category.SALARY,
                LocalDate.now()
        );

        service.addTransaction(salary);
        service.addTransaction(expenses);
        System.out.println(service.getAllTransactions());

        System.out.println("Income: " + service.getTotalIncome());
        System.out.println("Expenses: " + service.getTotalExpenses());
        System.out.println("Balance: " + service.getBalance());
    }
}