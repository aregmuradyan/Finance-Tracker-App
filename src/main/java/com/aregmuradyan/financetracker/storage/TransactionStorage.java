package com.aregmuradyan.financetracker.storage;

import com.aregmuradyan.financetracker.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TransactionStorage {
    private static final String FILE_NAME = "transactions.json";

    private final ObjectMapper objectMapper;

    public TransactionStorage() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void saveTransactions(List<Transaction> transactions) {

        try {

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(FILE_NAME),
                            transactions
                    );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> loadTransactions() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return List.of();
        }

        try {

            Transaction[] transactions =
                    objectMapper.readValue(
                            file,
                            Transaction[].class
                    );

            return Arrays.asList(transactions);

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
