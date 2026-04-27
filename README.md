# Accounting Ledger

A command-line accounting ledger application built with Java that allows users to track
their financial transactions through deposits and payments. The app provides a clean
interface to view, filter, and search through transaction history with various report options.

## Features

- **Add Deposits** - Record incoming money with description, vendor, and amount
- **Make Payments** - Record outgoing money (stored as negative amounts)
- **View Ledger** - See all transactions, filter by deposits or payments
- **Reports**
    - Month-to-Date - This month's transactions
    - Previous Month - Last month's transactions
    - Year-to-Date - This year's transactions
    - Previous Year - Last year's transactions
    - Search by Vendor - Find transactions by vendor name
- **Persistent Storage** - All transactions saved to CSV file

## How to Run

### Prerequisites
- Java 17 or higher
- IntelliJ IDEA (or any Java IDE)

### Steps
1. Clone this repository:
```bash
   git clone https://github.com/thejunyongjung/accounting-ledger.git
```
2. Open the project in IntelliJ IDEA
3. Run `Main.java`
4. Use the menu to navigate through the application

## Application Structure

```
accounting-ledger/
├── src/main/java/com/pluralsight/
│   ├── Main.java           # Entry point
│   ├── LedgerApp.java      # Main application logic
│   └── Transaction.java    # Transaction data model
└── transactions.csv        # Persistent transaction data
```

## Screens

### Home Screen
The main menu where users navigate to add transactions or view the ledger.

### Ledger Screen
Displays all transactions with options to filter:
- A) All transactions
- D) Deposits only
- P) Payments only
- R) Reports
- H) Back to home

### Reports Screen
Run pre-defined date filters or search by vendor name.

## Interesting Code Highlight

The `Transaction` class uses a custom `toCsvString()` method to convert objects
into CSV format for file storage:

```java
public String toCsvString() {
    return String.format("%s|%s|%s|%s|%.2f",
            this.date, this.time, this.description, this.vendor, this.amount);
}
```

This allows seamless conversion between in-memory objects and the CSV file format.

## Technologies Used

- Java 17
- Maven (build tool)
- Java I/O (BufferedReader, BufferedWriter)
- Java Time API (LocalDate, LocalTime)
- Java Collections (ArrayList)

## Future Improvements

- Add input validation for amounts
- Add custom search with multiple filters
- Add app theme/customization
- Implement data sorting by date

## Author

Junyong Jung