package Views;

import Models.*;
import utils.ConsoleUtils;
import enums.TransactionType;

import java.util.List;

public class ClientView {

    public int displayClientMenu() {
        String[] options = {
            "View Personal Information and Accounts",
            "Display Transaction History",
            "Calculate Balances and Totals",
            "View Suspicious Transactions",
            "Logout"
        };

        ConsoleUtils.displayMenu("Client Menu", options);
        return ConsoleUtils.readInt("Choose an option");
    }

    public void displayPersonalInfo(Client client) {
        System.out.println("\n==== Personal Information ====");
        System.out.println("Client ID: " + client.getId());
        System.out.println("Name: " + client.getFirstName() + " " + client.getLastName());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Number of accounts: " + (client.getAccounts() != null ? client.getAccounts().size() : 0));
    }

    public void displayAccounts(List<Account> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            showMessage("No accounts found.");
            return;
        }

        System.out.println("\n==== Your Bank Accounts ====");
        for (Account account : accounts) {
            System.out.println("Account ID: " + account.getAccountId());
            System.out.println("Type: " + account.getAccountType());
            System.out.println("Balance: $" + String.format("%.2f", account.getBalance()));
            System.out.println("--------------------");
        }
    }

    public void displayTransactionHistory(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            showMessage("No transactions found.");
            return;
        }

        System.out.println("\n==== Transaction History ====");
        for (Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Account: " + transaction.getAccountId());
            System.out.println("Type: " + transaction.getTransactionType());
            System.out.println("Amount: $" + String.format("%.2f", transaction.getAmount()));
            System.out.println("Date: " + transaction.getTimestamp());
            System.out.println("--------------------");
        }
    }

    public void displayTotals(double totalBalance, double totalDeposits, double totalWithdrawals) {
        System.out.println("\n==== Total Calculations ====");
        System.out.println("Total balance of all accounts: $" + String.format("%.2f", totalBalance));
        System.out.println("Total deposits: $" + String.format("%.2f", totalDeposits));
        System.out.println("Total withdrawals: $" + String.format("%.2f", totalWithdrawals));
        System.out.println("Net balance: $" + String.format("%.2f", totalDeposits - totalWithdrawals));
    }

    public void displaySuspiciousTransactions(List<Transaction> suspiciousTransactions) {
        System.out.println("\n==== Suspicious Transactions ====");
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
