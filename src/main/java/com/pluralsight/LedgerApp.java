package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    // Field
    private ArrayList<Transaction> transactions;
    private Scanner scanner;

    // Constructor
    public LedgerApp() {
        this.transactions = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // Method: Running the LedgerApp
    public void run() {
        loadTransactions();
        homeScreen();
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

    // Method: Homescreen
    private void homeScreen() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("==============================");
            System.out.println("Home Screen");
            System.out.println("==============================");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.println("------------------------------");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    ledgerScreen();
                    break;
                case "X":
                    System.out.println();
                    System.out.println("Exiting the Application...");
                    running = false;
                    break;
                default:
                    System.out.println();
                    System.out.println("Invalid choice. Try again!");
                    break;
            }
        }
        System.out.println("Thank you");
    }

    private void addDeposit() {
    }

    private void makePayment() {
    }

    private void ledgerScreen() {
    }
}
