package com.pluralsight;

import java.io.*;
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

    // Method: Reading CSV file (under run method)
    public void loadTransactions() {

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

    // Method: Homescreen (under run method)
    public void homeScreen() {
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

    // Method: Adding Deposit (under homeScreen method)
    public void addDeposit() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("Add Deposit");
        System.out.println("==============================");

        // Date | Time - Automatically
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().withNano(0);

        // Description | Vendor | Amount - User Input
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Enter amount: ");
        double amount = Math.abs(scanner.nextDouble());
        scanner.nextLine();

        // Create a transaction object for deposit
        Transaction deposit = new Transaction(date, time, description, vendor, amount);

        // Add to the Arraylist above
        this.transactions.add(deposit);

        // Append the deposit transaction to the transaction.csv
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            bw.write(deposit.toCsvString() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing transactions! " + e.getMessage());
            return;
        }

        // Confirmation Message to User
        System.out.println();
        System.out.println("Deposit added successfully!");
        System.out.println("------------------------------");
        System.out.println("Date:        " + deposit.getDate());
        System.out.println("Time:        " + deposit.getTime());
        System.out.println("Description: " + deposit.getDescription());
        System.out.println("Vendor:      " + deposit.getVendor());
        System.out.printf("Amount:      $%.2f%n", deposit.getAmount());
        System.out.println("------------------------------");
    }

    private void makePayment() {
    }

    private void ledgerScreen() {
    }
}
