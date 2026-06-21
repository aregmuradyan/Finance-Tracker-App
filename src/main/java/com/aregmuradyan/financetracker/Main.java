package com.aregmuradyan.financetracker;

import com.aregmuradyan.financetracker.model.Category;
import com.aregmuradyan.financetracker.model.Transaction;
import com.aregmuradyan.financetracker.model.TransactionType;
import com.aregmuradyan.financetracker.repository.TransactionRepository;
import com.aregmuradyan.financetracker.service.TransactionService;
import com.aregmuradyan.financetracker.ui.MainWindow;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
    /*    TransactionRepository repository = new TransactionRepository();
        TransactionService service = new TransactionService(repository);
        service.addTransaction(
                new Transaction(
                        1,
                        "Lunch",
                        "KFC",
                        5000,
                        "AMD",
                        TransactionType.EXPENSE,
                        Category.FOOD,
                        LocalDate.now()
                )
        );

        service.addTransaction(
                new Transaction(
                        2,
                        "Dinner",
                        "Pizza",
                        3000,
                        "AMD",
                        TransactionType.EXPENSE,
                        Category.FOOD,
                        LocalDate.now()
                )
        );

        service.addTransaction(
                new Transaction(
                        3,
                        "Taxi",
                        "Yandex",
                        2000,
                        "AMD",
                        TransactionType.EXPENSE,
                        Category.TRANSPORT,
                        LocalDate.now()
                )
        );
        System.out.println(service.getExpensesByCategory());//*/

        MainWindow.launch(MainWindow.class, args);
    }

}