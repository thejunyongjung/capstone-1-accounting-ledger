package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        // Append the deposit transaction to the transactions.csv
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv", true))) {
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

    // Method: Making Payment (under homeScreen method)
    private void makePayment() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("Make Payment");
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
        double amount = -Math.abs(scanner.nextDouble());
        scanner.nextLine();

        // Create a transaction object for deposit
        Transaction payment = new Transaction(date, time, description, vendor, amount);

        // Add to the Arraylist above
        this.transactions.add(payment);

        // Append the payment transaction to the transactions.csv
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv", true))) {
            bw.write(payment.toCsvString() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing transactions! " + e.getMessage());
            return;
        }

        // Confirmation Message to User
        System.out.println();
        System.out.println("Payment recorded successfully!");
        System.out.println("------------------------------");
        System.out.println("Date:        " + payment.getDate());
        System.out.println("Time:        " + payment.getTime());
        System.out.println("Description: " + payment.getDescription());
        System.out.println("Vendor:      " + payment.getVendor());
        System.out.printf("Amount:      $%.2f%n", payment.getAmount());
        System.out.println("------------------------------");
    }

    private void ledgerScreen() {
        boolean inLedger = true;
        while (inLedger) {
            System.out.println();
            System.out.println("==============================");
            System.out.println("Ledger Screen");
            System.out.println("==============================");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.println("------------------------------");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    displayAll();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsScreen();
                    break;
                case "H":
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again!");
                    break;
            }
        }
    }

    // Method: Display All Transactions (under ledgerScreen method)
    public void displayAll() {
        System.out.println();
        System.out.println("===================================================================================");
        System.out.println("All Transactions");
        printHeader();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            printTransaction(transactions.get(i));
        }
    }

    // Method: Display Deposit Transactions (under ledgerScreen method)
    public void displayDeposits() {
        System.out.println();
        System.out.println("===================================================================================");
        System.out.println("Deposit Transactions");
        printHeader();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount() > 0) {
                printTransaction(transaction);
            }
        }
    }

    // Method: Display Payment Transactions (under ledgerScreen method)
    public void displayPayments() {
        System.out.println();
        System.out.println("===================================================================================");
        System.out.println("Payment Transactions");
        printHeader();
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount() < 0) {
                printTransaction(transaction);
            }
        }
    }

    // Method: Display transaction header
    private void printHeader() {
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("%-12s %-10s %-25s %-20s %12s%n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------------");
    }

    // Method: Print transactions in an output format
    private void printTransaction(Transaction transaction) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.printf("%-12s %-10s %-25s %-20s $%10.2f%n",
                transaction.getDate(), transaction.getTime().format(timeFormat), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
    }

    // Method: Report Screen (under ledgerScreen Method)
    private void reportsScreen() {
        boolean inReports = true;
        while (inReports) {
            System.out.println();
            System.out.println("==============================");
            System.out.println("Reports");
            System.out.println("==============================");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.println("------------------------------");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    monthToDate();
                    break;
                case "2":
                    previousMonth();
                    break;
                case "3":
                    yearToDate();
                    break;
                case "4":
                    previousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again!");
                    break;
            }
        }
    }

    // Method: Display Month-to-Date Report (under reportScreen method)
    private void monthToDate() {
        System.out.println();
        System.out.println("===================================================================================");
        System.out.println("Month To Date");
        printHeader();

        LocalDate today = LocalDate.now();

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.getYear() == today.getYear() &&
                    transactionDate.getMonth() == today.getMonth()) {
                printTransaction(transaction);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No transactions found!");
        }
    }

    // Method: Display Previous Month Report (under reportScreen method)
    private void previousMonth() {
        System.out.println();
        System.out.println("===================================================================================");
        System.out.println("Previous Month");
        printHeader();

        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.getYear() == lastMonth.getYear() &&
                    transactionDate.getMonth() == lastMonth.getMonth()) {
                printTransaction(transaction);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No transactions found in previous month!");
        }
    }

    // Method: Display Year-to-Date Report (under reportScreen method)
    private void yearToDate() {
        System.out.println();
        System.out.println("===================================================================================");
        System.out.println("Year To Date");
        printHeader();

        LocalDate today = LocalDate.now();

        int count = 0;
        for (int i = transactions.size()-1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.getYear() == today.getYear()) {
                printTransaction(transaction);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No transactions found!");
        }
    }

    // Method: Display Previous Year Report (under reportScreen method)
    private void previousYear() {
        System.out.println();
        System.out.println("===================================================================================");
        System.out.println("Previous Year");
        printHeader();

        LocalDate lastYear = LocalDate.now().minusYears(1);

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.getYear() == lastYear.getYear()) {
                printTransaction(transaction);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No transactions found in previous year!");
        }
    }

    // Method: Display Report by Vendor (under reportScreen method)
    private void searchByVendor() {
        System.out.println();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("===================================================================================");
        System.out.println("Search Results for " + vendor);
        printHeader();

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                printTransaction(transaction);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No matching transactions found for vendor: " + vendor);
        }

    }
}
