package com.pluralsight;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    // FIELD
    private ArrayList<Transaction> transactions;
    private Scanner scanner;

    // CONSTRUCTOR
    public LedgerApp() {
        this.transactions = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // ANSI COLOR CODES FOR CLI APP DESIGN
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String BR_CYAN = "\u001B[96m";
    private static final String BR_MAGENTA = "\u001B[95m";

    // DEFINE CONSTANT NEEDED AT A CLASS LEVEL
    private static final String CSV_FILE = "transactions.csv";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final double AMOUNT_BUFFER = 5.0;

    // METHOD: ===== APP START =====
    public void run() {
        showSplashScreen();
        loadTransactions();
        homeScreen();
    }

    // METHOD: SPLASH SCREEN (UNDER 'run' METHOD)
    private void showSplashScreen() {

        System.out.println(BR_CYAN + BOLD);

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║                  ACCOUNTING LEDGER v1.0                    ║");
        System.out.println("║                                                            ║");
        System.out.println("║          Track your money. Master your finances.           ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        System.out.println(RESET);

        // Pause for effect
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) { /* ignore interruption */ }
    }

    // METHOD: ===== FILE I/O ===== (UNDER 'run' METHOD)
    public void loadTransactions() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;

            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {

                // Skip invalid lines
                if (line.trim().isEmpty()) continue;

                String[] data = line.split("\\|");
                if (data.length < 5) continue;

                // Parse data
                LocalDate date = LocalDate.parse(data[0]);
                LocalTime time = LocalTime.parse(data[1]);
                String description = data[2];
                String vendor = data[3];
                double amount = Double.parseDouble(data[4]);

                // Add transaction
                transactions.add(new Transaction(date, time, description, vendor, amount));
            }
        } catch (IOException e) {
            System.out.println(RED + "Error loading transactions! " + e.getMessage() + RESET);
        }
    }

    /** ===== HOMESCREEN ===== */
    // METHOD: HOMESCREEN (UNDER 'run' METHOD)
    private void homeScreen() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println(CYAN + "╔══════════════════════════════╗");
            System.out.println("║         HOME SCREEN          ║");
            System.out.println("╚══════════════════════════════╝" + RESET);
            System.out.println("  " + GREEN + "D" + RESET + ") Add Deposit");
            System.out.println("  " + RED + "P" + RESET + ") Make Payment (Debit)");
            System.out.println("  " + BLUE + "L" + RESET + ") Ledger");
            System.out.println("  " + YELLOW + "X" + RESET + ") Exit");
            System.out.println("  ────────────────────────────");

            System.out.print("  Enter your choice: ");
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
                    System.out.println(BR_CYAN + "  Exiting the Application..." + RESET);
                    running = false;
                    break;
                default:
                    System.out.println();
                    System.out.println(YELLOW + "  Invalid choice. Try again!" + RESET);
                    break;
            }
        }
        System.out.println(GREEN + BOLD + "\n  Thank you for using Accounting Ledger! 👋\n" + RESET);
    }

    // METHOD: ADD DEPOSIT (UNDER 'homeScreen' METHOD)
    private void addDeposit() {
        System.out.println();
        System.out.println(GREEN + "══════════════════════════════");
        System.out.println("        ADD DEPOSIT");
        System.out.println("══════════════════════════════" + RESET);

        // Date | Time - Automatically
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().withNano(0);

        // Description | Vendor | Amount - User Input
        String description = promptString("description");

        String vendor = promptString("vendor");

        double amount = promptAmount(true);

        // Create a new transaction object for deposit
        Transaction deposit = new Transaction(date, time, description, vendor, amount);

        // Add it to the Arraylist<Transaction> above
        this.transactions.add(deposit);

        // Save & Print Deposit Transaction
        saveTransaction(deposit);
        printConfirmation("Deposit added successfully! 💵", deposit, GREEN);
    }

    // METHOD: MAKE PAYMENT (UNDER 'homeScreen' METHOD)
    private void makePayment() {
        System.out.println();
        System.out.println(RED + "══════════════════════════════");
        System.out.println("       MAKE PAYMENT");
        System.out.println("══════════════════════════════" + RESET);

        // Date | Time - Automatically
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().withNano(0);

        // Description | Vendor | Amount - User Input
        String description = promptString("description");

        String vendor = promptString("vendor");

        double amount = promptAmount(false);

        // Create a new transaction object for payment
        Transaction payment = new Transaction(date, time, description, vendor, amount);

        // Add it to the Arraylist<Transaction> above
        this.transactions.add(payment);

        // Save & Print Payment Transaction
        saveTransaction(payment);
        printConfirmation("Payment recorded successfully! 💸", payment, RED);
    }

    /** ===== HELPER METHOD ===== */
    // METHOD: SAVE TRANSACTION (UNDER 'addDeposit', 'makePayment' METHOD)
    private void saveTransaction(Transaction _transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            bw.write(_transaction.toCsvString() + "\n");
        } catch (IOException e) {
            System.out.println(RED + "  Error saving transaction! " + e.getMessage() + RESET);
        }
    }

    // METHOD: PRINT CONFIRMATION FOR USER (UNDER 'addDeposit', 'makePayment' METHOD)
    private void printConfirmation(String _title, Transaction _transaction, String _color) {
        System.out.println();
        System.out.println(_color + BOLD + "  ✓ " + _title + RESET);
        System.out.println("  ──────────────────────────────");
        System.out.println("  Date:        " + _transaction.getDate());
        System.out.println("  Time:        " + _transaction.getTime().format(TIME_FORMAT));
        System.out.println("  Description: " + _transaction.getDescription());
        System.out.println("  Vendor:      " + _transaction.getVendor());
        System.out.printf("  Amount:      %s$%,.2f%s%n", _color, _transaction.getAmount(), RESET);
        System.out.println("  ──────────────────────────────");
    }

    // METHOD: PROMPT USER FOR A VALID INPUT (UNDER 'addDeposit', 'makePayment', 'searchByVendor', BONUS METHOD)
    private String promptString(String _fieldName) {
        String input = "";
        boolean validIput = false;

        while (!validIput) {
            System.out.print("  Enter " + _fieldName + ": ");
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(YELLOW + "  '" + _fieldName + "' can't be empty or blank. Please try again!" + RESET);
            } else {
                validIput = true;
            }
        }
        return input;
    }

    // Method: PROMPT USER FOR A VALID AMOUNT (UNDER 'addDeposit', 'makePayment' METHOD)
    private double promptAmount(boolean _isDeposit) {
        double amount = 0;
        boolean validAmount = false;

        while (!validAmount) {
            System.out.print("  Enter amount: ");
            amount = Math.abs(Double.parseDouble(scanner.nextLine().trim()));

            // Handling zero-amount transaction
            if (amount == 0) {
                System.out.println(YELLOW + "  Amount cannot be zero. Please try again!" + RESET);
            } else {
                validAmount = true;
            }
        }
        // For Payment, return as negative
        return _isDeposit ? amount : -amount;
    }

    /** ===== LEDGER SCREEN ===== */
    // METHOD: LEDGER SCREEN (UNDER 'homeScreen' METHOD)
    private void ledgerScreen() {
        boolean inLedger = true;
        while (inLedger) {
            System.out.println();
            System.out.println(BLUE + "╔══════════════════════════════╗");
            System.out.println("║        LEDGER SCREEN         ║");
            System.out.println("╚══════════════════════════════╝" + RESET);
            System.out.println("  " + CYAN + "A" + RESET + ") All Transactions");
            System.out.println("  " + GREEN + "D" + RESET + ") Deposits Only");
            System.out.println("  " + RED + "P" + RESET + ") Payments Only");
            System.out.println("  " + PURPLE + "R" + RESET + ") Reports");
            System.out.println("  " + YELLOW + "H" + RESET + ") Home");
            System.out.println("  ────────────────────────────");
            System.out.print("  Enter your choice: ");
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
                    System.out.println("  Invalid choice. Try again!");
                    break;
            }
        }
    }

    // METHOD: DISPLAY ALL TRANSACTIONS (UNDER 'ledgerScreen' METHOD)
    private void displayAll() {
        printSectionHeader("All Transactions", CYAN);
        printHeader();
        int count = 0;
        // For loop - Reverse order (newest transaction first)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            printTransaction(transactions.get(i));
            count++;
        }
        printFooter(count);
    }

    // METHOD: DISPLAY DEPOSIT TRANSACTIONS (UNDER 'ledgerScreen' METHOD)
    private void displayDeposits() {
        printSectionHeader("Deposit Transactions", GREEN);
        printHeader();
        int count = 0;
        // For loop - Reverse order (newest transaction first)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount() > 0) {
                printTransaction(transaction);
                count++;
            }
        }
        printFooter(count);
    }

    // METHOD: DISPLAY PAYMENT TRANSACTIONS (UNDER 'ledgerScreen' METHOD)
    private void displayPayments() {
        printSectionHeader("Payment Transactions", RED);
        printHeader();
        int count = 0;
        // For loop - Reverse order (newest transaction first)
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount() < 0) {
                printTransaction(transaction);
                count++;
            }
        }
        printFooter(count);
    }

    /** ===== REPORTS SCREEN ===== */
    // METHOD: REPORTS SCREEN (UNDER 'ledgerScreen' METHOD)
    private void reportsScreen() {
        boolean inReports = true;
        while (inReports) {
            System.out.println();
            System.out.println(PURPLE + "╔══════════════════════════════╗");
            System.out.println("║          REPORTS             ║");
            System.out.println("╚══════════════════════════════╝" + RESET);
            System.out.println("  1) Month To Date");
            System.out.println("  2) Previous Month");
            System.out.println("  3) Year To Date");
            System.out.println("  4) Previous Year");
            System.out.println("  5) Search by Vendor");
            System.out.println("  6) Custom Search " + CYAN + "(NEW!)" + RESET);
            System.out.println("  0) Back");
            System.out.println("  ────────────────────────────");
            System.out.print("  Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": monthToDate(); break;
                case "2": previousMonth(); break;
                case "3": yearToDate(); break;
                case "4": previousYear(); break;
                case "5": searchByVendor(); break;
                case "6": customSearch(); break;
                case "0": inReports = false; break;
                default:
                    System.out.println(YELLOW + "  Invalid choice. Try again!" + RESET);
                    break;
            }
        }
    }

    // METHOD: DISPLAY MONTH-TO-DATE REPORT (UNDER 'reportScreen' METHOD)
    private void monthToDate() {
        printSectionHeader("Month To Date", PURPLE);
        printHeader();

        LocalDate today = LocalDate.now();

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            // t.getDate() --> Getter
            if (t.getDate().getYear() == today.getYear() &&
                    t.getDate().getMonth() == today.getMonth()) {
                printTransaction(t);
                count++;
            }
        }
        printFooter(count);
    }

    // METHOD: DISPLAY PREVIOUS MONTH REPORT (UNDER 'reportScreen' METHOD)
    private void previousMonth() {
        printSectionHeader("Previous Month", PURPLE);
        printHeader();

        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == lastMonth.getYear() &&
                    t.getDate().getMonth() == lastMonth.getMonth()) {
                printTransaction(t);
                count++;
            }
        }
        printFooter(count);
    }

    // METHOD: DISPLAY YEAR-TO-DATE REPORT (UNDER 'reportScreen' METHOD)
    private void yearToDate() {
        printSectionHeader("Year To Date", PURPLE);
        printHeader();

        int currentYear = LocalDate.now().getYear();

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == currentYear) {
                printTransaction(t);
                count++;
            }
        }
        printFooter(count);
    }

    // METHOD: DISPLAY PREVIOUS YEAR REPORT (UNDER 'reportScreen' METHOD)
    private void previousYear() {
        printSectionHeader("Previous Year", PURPLE);
        printHeader();

        int lastYear = LocalDate.now().getYear() - 1;
        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == lastYear) {
                printTransaction(t);
                count++;
            }
        }
        printFooter(count);
    }

    // METHOD: DISPLAY REPORT BY VENDOR (UNDER 'reportScreen', 'customSearch (BONUS)' METHOD)
    private void searchByVendor() {
        System.out.println();
        String vendor = promptString("Vendor");

        printSectionHeader("Search Results for: " + vendor, PURPLE);
        printHeader();

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);
            if (t.getVendor().equalsIgnoreCase(vendor)) {
                printTransaction(t);
                count++;
            }
        }
        printFooter(count);
    }

    /** ===== BONUS: CUSTOM SEARCH ===== */
    // BONUS METHOD: CUSTOM SEARCH BY DIFFERENT USER INPUT
    private void customSearch() {
        System.out.println();
        System.out.println(BR_MAGENTA + "╔══════════════════════════════╗");
        System.out.println("║       CUSTOM SEARCH          ║");
        System.out.println("╚══════════════════════════════╝" + RESET);
        System.out.println("  Type fields to filter,");
        System.out.println("  or press Enter to skip");
        System.out.println("  ────────────────────────────");

        // Get 5 inputs from the user
        String startDateInput = promptOptional("Start Date (YYYY-MM-DD)");
        String endDateInput = promptOptional("End Date (YYYY-MM-DD)");
        String descriptionInput = promptOptional("Description");
        String vendorInput = promptOptional("Vendor");
        String amountInput = promptOptional("Amount (+/- $5)");

        // Convert dates and amount (used 'Double' class to store null)
        LocalDate startDate = startDateInput.isEmpty() ? null : LocalDate.parse(startDateInput);
        LocalDate endDate = endDateInput.isEmpty() ? null : LocalDate.parse(endDateInput);
        Double amount = amountInput.isEmpty() ? null : Double.parseDouble(amountInput);

        printSectionHeader("Custom Search Results", BR_MAGENTA);
        printHeader();

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            // ===== FILTER 1: START DATE =====
            if (startDate != null) {
                if (t.getDate().isBefore(startDate)) continue;
            }

            // ===== FILTER 2: END DATE =====
            if (endDate != null) {
                if (t.getDate().isAfter(endDate)) continue;
            }

            // ===== FILTER 3: DESCRIPTION =====
            if (!descriptionInput.isEmpty()) {
                if (!t.getDescription().toLowerCase()
                        .contains(descriptionInput.toLowerCase())) continue;
            }

            // ===== FILTER 4: VENDOR =====
            if (!vendorInput.isEmpty()) {
                if (!t.getVendor().toLowerCase()
                        .contains(vendorInput.toLowerCase())) continue;
            }

            // ===== FILTER 5: AMOUNT =====
            if (amount != null) {
                if (Math.abs(t.getAmount() - amount) > AMOUNT_BUFFER) continue;
            }
            printTransaction(t);
            count++;
        }
        printFooter(count);
    }

    // BONUS HELPER METHOD: ACCEPTS EMPTY INPUT AS "SKIP"
    private String promptOptional(String _fieldName) {
        System.out.println();
        System.out.print("  Enter " + _fieldName + " [Press Enter to skip]: ");
        return scanner.nextLine().trim();
    }

    /** ===== DISPLAY HELPERS ===== */
    // METHOD: DISPLAY SECTION HEADER
    private void printSectionHeader(String _title, String _color) {
        System.out.println();
        System.out.println(_color + BOLD +
                "═══════════════════════════════════════════════════════════════════════════════════" + RESET);
        System.out.println(_color + BOLD + "  " + _title + RESET);
    }

    // METHOD: DISPLAY TRANSACTION HEADER
    private void printHeader() {
        System.out.println("───────────────────────────────────────────────────────────────────────────────────");
        System.out.printf(BOLD + "%-12s %-10s %-25s %-20s %-12s%n" + RESET,
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("───────────────────────────────────────────────────────────────────────────────────");
    }

    // METHOD: PRINT TRANSACTIONS IN A SPECIFIC OUTPUT FORMAT
    private void printTransaction(Transaction _transaction) {
        String amountColor = _transaction.getAmount() > 0 ? GREEN : RED;
        String description = _transaction.getDescription();
        String vendor = _transaction.getVendor();

        // Truncate long fields to keep alignment clean
        if (description.length() > 24) {
            description = description.substring(0, 22) + "..";
        }
        if (vendor.length() > 19) {
            vendor = vendor.substring(0, 17) + "..";
        }

        // Output
        System.out.printf("%-12s %-10s %-25s %-20s %s$%10.2f%s%n",
                _transaction.getDate(),
                _transaction.getTime().format(TIME_FORMAT),
                description,
                vendor,
                amountColor,
                _transaction.getAmount(), RESET);
    }

    // METHOD: DISPLAY PRINT FOOTER
    private void printFooter(int _count) {
        System.out.println("───────────────────────────────────────────────────────────────────────────────────");
        if (_count == 0) {
            System.out.println(YELLOW + "  No transactions found." + RESET);
        } else if (_count == 1) {
            System.out.println(CYAN + "  Total: " + _count + " transaction" + RESET);
        } else {
            System.out.println(CYAN + "  Total: " + _count + " transactions" + RESET);
        }
    }
}

