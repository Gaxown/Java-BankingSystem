package Views;

import Models.*;
import utils.ConsoleUtils;
import enums.AccountType;
import enums.TransactionType;

import java.util.List;

public class ManagerView {

    public int displayManagerMenu() {
        String[] options = {
            "Manage Clients and Accounts",
            "Manage Transactions",
            "Consult Transactions",
            "View Suspicious Transactions",
            "Logout"
        };

        ConsoleUtils.displayMenu("Manager Menu", options);
        return ConsoleUtils.readInt("Choose an option");
    }

    public int displayClientManagementMenu() {
        String[] options = {
            "Create Client",
            "Modify Client",
            "Delete Client",
            "Create Account for Client",
            "Delete Client Account",
            "Back to Main Menu"
        };

        ConsoleUtils.displayMenu("Client Management", options);
        return ConsoleUtils.readInt("Choose an option");
    }

    public int displayTransactionManagementMenu() {
        String[] options = {
            "Add Transaction",
            "Back to Main Menu"
        };

        ConsoleUtils.displayMenu("Transaction Management", options);
        return ConsoleUtils.readInt("Choose an option");
    }

    public int displayConsultationMenu() {
        String[] options = {
            "All client transactions",
            "Transactions for specific account"
        };

        ConsoleUtils.displayMenu("Transaction Consultation", options);
        return ConsoleUtils.readInt("Choose an option");
    }

    public int displaySuspiciousTransactionsMenu() {
        String[] options = {
            "View suspicious transactions for specific client",
            "View all suspicious transactions across all clients"
        };

        ConsoleUtils.displayMenu("Suspicious Transactions", options);
        return ConsoleUtils.readInt("Choose an option");
    }

    public AccountType selectAccountType() {
        System.out.println("\n==== Select Account Type ====");
        AccountType[] types = AccountType.values();

        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }

        int choice = ConsoleUtils.readInt("Select type (1-" + types.length + ")");

        if (choice >= 1 && choice <= types.length) {
            return types[choice - 1];
        } else {
            showError("Invalid selection. Defaulting to SAVINGS.");
            return AccountType.SAVINGS;
        }
    }

    public TransactionType selectTransactionType() {
        System.out.println("\n==== Select Transaction Type ====");
        TransactionType[] types = TransactionType.values();

        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }

        int choice = ConsoleUtils.readInt("Select type (1-" + types.length + ")");

        if (choice >= 1 && choice <= types.length) {
            return types[choice - 1];
        } else {
            showError("Invalid selection. Defaulting to DEPOSIT.");
            return TransactionType.DEPOSIT;
        }
    }

    public boolean selectSortOrder() {
        System.out.println("\n==== Sort Order ====");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");

        int choice = ConsoleUtils.readInt("Choose order");
        return choice == 1;
    }

    public void displayTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            showMessage("No transactions found.");
            return;
        }

        System.out.println("\n==== Transactions ====");
        for (Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Account: " + transaction.getAccountId());
            System.out.println("Type: " + transaction.getTransactionType());
            System.out.println("Amount: $" + String.format("%.2f", transaction.getAmount()));
            System.out.println("Date: " + transaction.getTimestamp());
            System.out.println("--------------------");
        }
    }

    public void displaySuspiciousTransactions(List<Transaction> suspiciousTransactions, String clientInfo) {
        System.out.println("\n==== Suspicious Transactions - " + clientInfo + " ====");
        System.out.println("The following transactions have been flagged as suspicious:");

        for (Transaction transaction : suspiciousTransactions) {
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Account: " + transaction.getAccountId());
            System.out.println("Type: " + transaction.getTransactionType());
            System.out.println("Amount: $" + String.format("%.2f", transaction.getAmount()));
            System.out.println("Date: " + transaction.getTimestamp());

            // Show reason for being suspicious
            if (transaction.getAmount() > 10000) {
                System.out.println("Reason: High amount transaction (over $10,000)");
            } else {
                System.out.println("Reason: Repetitive transaction pattern detected");
            }
            System.out.println("--------------------");
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String error) {
        System.out.println("Error: " + error);
    }

    public void showSuccessMessage(String message) {
        System.out.println("Success: " + message);
    }
}
