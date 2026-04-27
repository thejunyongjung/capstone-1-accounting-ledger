package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class LedgerApp {
    // Field
    private ArrayList<Transaction> transactions;

    // Constructor
    public LedgerApp() {
        this.transactions = new ArrayList<>();
    }

    // Method: Running the Applicaion
    public void run() {
        loadTransactions();
    }

    // Method: Reading CSV file
    private void loadTransactions() {

        String fileName = "transactions.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Remove header

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                LocalDate date = LocalDate.parse(data[0]);
                LocalTime time = LocalTime.parse(data[1]);
                String description = data[2];
                String vendor = data[3];
                double amount = Double.parseDouble(data[4]);

                transactions.add(new Transaction(date, time, description, vendor, amount));
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions! " + e.getMessage());
        }
    }
}
