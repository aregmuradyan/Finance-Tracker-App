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
        MainWindow.launch(MainWindow.class, args);
    }

}