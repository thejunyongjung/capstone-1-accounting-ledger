# 💰 Accounting Ledger

> A professional command-line accounting application built in Java for tracking, filtering, and analyzing financial transactions.

![Java](https://img.shields.io/badge/Java-17-orange)
![Build](https://img.shields.io/badge/Build-Maven-blue)
![Status](https://img.shields.io/badge/Status-Complete-success)

---

## 📖 Overview

**Accounting Ledger** is a command-line Java application that helps users record and review their financial activity. It supports adding deposits and payments, generating period-based reports, and a powerful **multi-field custom search** that filters transactions across date ranges, descriptions, vendors, and amounts — all from one form.

The app emphasizes **defensive programming**: every input is validated, every error is handled gracefully, and the app never crashes on bad input.

---

## ✨ Features

### Core Features
- 💵 **Add Deposits** — Record incoming money with description, vendor, and amount
- 💸 **Make Payments** — Record outgoing money (stored as negative amounts)
- 📊 **Live Balance Display** — Current balance shown on home screen with color indicator
- 📋 **Ledger Views** — All transactions, deposits only, or payments only
- 💾 **Persistent Storage** — All transactions saved to CSV file automatically

### Reports
- 📅 Month-to-Date — This month's transactions
- 🗓️ Previous Month — Last month's transactions
- 📈 Year-to-Date — This year's transactions
- 🗂️ Previous Year — Last year's transactions
- 🔍 Search by Vendor — Find transactions by vendor name (case-insensitive)

### 🌟 Bonus: Custom Search
A powerful multi-field search where users can filter by **any combination** of:
- Start Date / End Date (date range)
- Description (case-insensitive substring match)
- Vendor (case-insensitive substring match)
- Amount (within a $5 buffer)

Users can leave any field blank to skip that filter — making this both flexible and intuitive.

### 🛡️ Robust Error Handling
- ✅ Invalid date formats are caught and re-prompted (e.g., `april 1` → ask again)
- ✅ Non-numeric amounts are caught and re-prompted (e.g., `abc` → ask again)
- ✅ Empty description/vendor fields are rejected
- ✅ Zero-amount transactions are rejected
- ✅ The app never crashes on bad input

---

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- Maven (or any Java IDE such as IntelliJ IDEA)

### Steps
1. Clone this repository:
   ```bash
   git clone https://github.com/thejunyongjung/capstone-1-accounting-ledger.git
   ```
2. Open the project in IntelliJ IDEA
3. Run `Main.java`
4. Use the menu to navigate through the application

---

## 📂 Project Structure

```
capstone-1-accounting-ledger/
├── src/main/java/com/pluralsight/
│   ├── Main.java           # Entry point
│   ├── LedgerApp.java      # Main application logic & menus
│   └── Transaction.java    # Transaction data model
├── transactions.csv        # Persistent transaction data
├── pom.xml                 # Maven configuration
└── README.md
```

---

## 🎨 Application Screens

### Home Screen
Displays the current balance, transaction count, and primary navigation menu.

### Ledger Screen
Shows transactions in reverse chronological order (newest first), with green for deposits and red for payments.

### Reports Screen
Pre-defined date filters and search options, plus the bonus Custom Search.

---

## 🔍 Code Highlight: The `promptAmount()` Helper

When designing `addDeposit()` and `makePayment()`, I noticed they had nearly identical logic for collecting and validating amounts. Instead of duplicating code, I extracted one helper:

```java
private double promptAmount(boolean _isDeposit) {
    while (true) {
        System.out.print("  Enter amount: ");
        String amountInput = scanner.nextLine().trim();

        try {
            double amount = Math.abs(Double.parseDouble(amountInput));

            if (amount == 0) {
                System.out.println(YELLOW + "  Amount can't be zero. Please try again!" + RESET);
                continue;
            }

            // For Payment, return as negative
            return _isDeposit ? amount : -amount;

        } catch (NumberFormatException e) {
            System.out.println(YELLOW + "  Amount must be numeric. Please enter valid amount!" + RESET);
        }
    }
}
```

The clever part is the boolean parameter `_isDeposit`:
- `addDeposit()` calls `promptAmount(true)` → gets a positive number
- `makePayment()` calls `promptAmount(false)` → gets a negative number

**One method, two behaviors, zero duplication.** Plus, the error handling lives in one place — if I ever need to update validation rules, I update them once.

---

## 🏛️ Design Decisions

### Why is `transactions` a class field?
The transactions list is shared across nearly every method (`addDeposit`, `displayAll`, `customSearch`, etc.). Making it a field provides a **single source of truth** for the data and avoids passing it as a parameter through dozens of methods.

### Why is `Scanner` a field, and why isn't it closed?
The `Scanner` is created once in the constructor and reused across all methods. It is **deliberately not closed** because closing a Scanner that wraps `System.in` would close the keyboard input stream itself — which cannot be reopened. Since the Scanner's lifecycle matches the program's, the JVM handles cleanup at exit.

### Why use a custom `toCsvString()` method?
The `Transaction` class includes a `toCsvString()` method that converts an object into a pipe-separated line. This serves as a simple bridge between in-memory objects and the CSV file:

```java
public String toCsvString() {
    return String.format("%s|%s|%s|%s|%.2f",
            this.date, this.time, this.description, this.vendor, this.amount);
}
```

When loading, the same format is reversed using `String.split("\\|")`.

---

## 🛠️ Technologies Used

- **Java 17** — Core language
- **Maven** — Build & dependency management
- **Java I/O** — `BufferedReader`, `BufferedWriter` for file I/O
- **Java Time API** — `LocalDate`, `LocalTime` for date handling
- **Java Collections** — `ArrayList<Transaction>` for in-memory storage
- **ANSI Escape Codes** — For colored CLI output

---

## 📚 What I Learned

1. **DRY Principle** — Extracted helper methods (`promptAmount`, `promptString`, `promptBonus*`) to eliminate duplicate code.
2. **Defensive Programming** — Every user input is validated; the app handles bad input gracefully without crashing.
3. **Encapsulation** — All fields are `private` and accessed through getters/setters in the `Transaction` class.
4. **Separation of Concerns** — `Main.java` only starts the app, `LedgerApp.java` handles UI/logic, `Transaction.java` is a pure data model.
5. **CSV as a simple persistence layer** — A pipe-separated format avoids conflicts with commas in descriptions.

---

## 🔮 Future Improvements

- [ ] Sort transactions by date dynamically (currently relies on file order)
- [ ] Support CSV escaping for descriptions containing pipe characters
- [ ] Add transaction editing and deletion
- [ ] Export reports to PDF or Excel
- [ ] Multi-account support
- [ ] Currency support (USD, EUR, KRW, etc.)

---

## 👤 Author

**Junyong Jung**
[GitHub](https://github.com/thejunyongjung)

---

## 📄 License

This project was built for educational purposes as part of the Pluralsight Year-Up Java capstone.