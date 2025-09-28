package Controllers;

import Models.*;
import Services.BankingService;
import Views.ClientView;
import utils.ConsoleUtils;

import java.util.List;

public class ClientController implements UserController {

    private BankingService bankingService;
    private ClientView clientView;

    public ClientController() {
        this.bankingService = BankingService.getInstance();
        this.clientView = new ClientView();
    }

    public void handleClientMenu() {
        while (true) {
            int choice = clientView.displayClientMenu();

            switch (choice) {
                case 1:
                    viewPersonalInfoAndAccounts();
                    break;
                case 2:
                    viewTransactionHistory();
                    break;
                case 3:
                    calculateTotals();
                    break;
                case 4:
                    viewSuspiciousTransactions();
                    break;
                case 5:
                    logout();
                    return;
                default:
                    clientView.showError("Invalid choice. Please try again.");
            }
        }
    }

    private void viewPersonalInfoAndAccounts() {
        User currentUser = bankingService.user();
        if (currentUser != null && currentUser instanceof Client) {
            Client client = (Client) currentUser;
            clientView.displayPersonalInfo(client);

            List<Account> accounts = bankingService.getAccountsByClientId(client.getId());
            clientView.displayAccounts(accounts);
        } else {
            clientView.showError("login required");
        }
    }

    private void viewTransactionHistory() {
        try {
            User currentUser = bankingService.user();
            if (currentUser != null && currentUser instanceof Client) {
                Client client = (Client) currentUser;
                List<Account> accounts = bankingService.getAccountsByClientId(client.getId());

                if (accounts == null || accounts.isEmpty()) {
                    clientView.showMessage("No accounts found.");
                    return;
                }


                List<Transaction> allTransactions = bankingService.getAllTransactionsForClient(client.getId());
                clientView.displayTransactionHistory(allTransactions);

            } else {
                clientView.showError("No client logged in.");
            }
        } catch (Exception e) {
            clientView.showError("Error retrieving transaction history: " + e.getMessage());
        }
    }

    private void calculateTotals() {
        try {
            User currentUser = bankingService.user();
            if (currentUser != null && currentUser instanceof Client) {
                Client client = (Client) currentUser;

                double totalBalance = bankingService.calculateTotalBalance(client.getId());
                double totalDeposits = bankingService.calculateTotalDeposits(client.getId());
                double totalWithdrawals = bankingService.calculateTotalWithdrawals(client.getId());

                clientView.displayTotals(totalBalance, totalDeposits, totalWithdrawals);

            } else {
                clientView.showError("No logged in.");
            }
        } catch (Exception e) {
            clientView.showError("Error calculating totals: " + e.getMessage());
        }
    }

    private void viewSuspiciousTransactions() {
        try {
            User currentUser = bankingService.user();
            if (currentUser != null && currentUser instanceof Client) {
                Client client = (Client) currentUser;
                List<Transaction> suspiciousTransactions = bankingService.detectSuspiciousTransactions(client.getId());

                if (suspiciousTransactions.isEmpty()) {
                    clientView.showMessage("No suspicious transactions found.");
                } else {
                    clientView.displaySuspiciousTransactions(suspiciousTransactions);
                }
            } else {
                clientView.showError("No client logged in.");
            }
        } catch (Exception e) {
            clientView.showError("Error retrieving suspicious transactions: " + e.getMessage());
        }
    }

    @Override
    public void viewProfile() {
        viewPersonalInfoAndAccounts();
    }

    @Override
    public void updateProfile() {
        clientView.showMessage("xxxxxxxxxxxxxx");
    }

    @Override
    public void changePassword() {
        clientView.showMessage("xxxxxxxxxxxxxx");
    }

    @Override
    public void viewTransactions() {
        viewTransactionHistory();
    }

    private void logout() {
        bankingService.logout();
        clientView.showSuccessMessage("Logged out successfully.");
    }
}
