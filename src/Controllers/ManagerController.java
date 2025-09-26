package Controllers;

import Models.*;
import Services.BankingService;
import Views.ManagerView;
import utils.ConsoleUtils;
import enums.AccountType;
import enums.TransactionType;

import java.util.List;
import java.util.UUID;

public class ManagerController {

    private BankingService bankingService;
    private ManagerView managerView;

    public ManagerController() {
        this.bankingService = BankingService.getInstance();
        this.managerView = new ManagerView();
    }

    public void handleManagerMenu() {
        while (true) {
            int choice = managerView.displayManagerMenu();

            switch (choice) {
                case 1:
                    manageClients();
                    break;
                case 2:
                    manageTransactions();
                    break;
                case 3:
                    consultTransactions();
                    break;
                case 4:
                    logout();
                    return;
                default:
                    managerView.showError("Invalid choice. Please try again.");
            }
        }
    }

    private void manageClients() {
        while (true) {
            int choice = managerView.displayClientManagementMenu();

            switch (choice) {
                case 1:
                    createClient();
                    break;
                case 2:
                    modifyClient();
                    break;
                case 3:
                    deleteClient();
                    break;
                case 4:
                    createAccountForClient();
                    break;
                case 5:
                    deleteClientAccount();
                    break;
                case 6:
                    return; // Back to main menu
                default:
                    managerView.showError("Invalid choice. Please try again.");
            }
        }
    }

    private void manageTransactions() {
        while (true) {
            int choice = managerView.displayTransactionManagementMenu();

            switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    return; // Back to main menu
                default:
                    managerView.showError("Invalid choice. Please try again.");
            }
        }
    }

    private void consultTransactions() {
        try {
            String clientEmail = ConsoleUtils.readEmail("Enter client email");
            User client = bankingService.getUserByEmail(clientEmail);

            if (client == null || !client.getRole().equals(enums.Role.CLIENT)) {
                managerView.showError("Client not found.");
                return;
            }

            int choice = managerView.displayConsultationMenu();

            switch (choice) {
                case 1:
                    // All transactions for all client accounts
                    List<Transaction> allTransactions = bankingService.getAllTransactionsForClient(client.getId());
                    managerView.displayTransactions(allTransactions);
                    break;
                case 2:
                    // Transactions for specific account
                    String accountId = ConsoleUtils.readString("Enter account ID");
                    List<Transaction> accountTransactions = bankingService.getTransactionsByAccountId(UUID.fromString(accountId));
                    managerView.displayTransactions(accountTransactions);
                    break;
                default:
                    managerView.showError("Invalid choice.");
            }

        } catch (Exception e) {
            managerView.showError("Error consulting transactions: " + e.getMessage());
        }
    }

    private void createClient() {
        try {
            String firstName = ConsoleUtils.readString("First Name");
            String lastName = ConsoleUtils.readString("Last Name");
            String email = ConsoleUtils.readEmail("Email");
            String password = ConsoleUtils.readString("Password");

            User newClient = bankingService.createClient(firstName, lastName, email, password);
            managerView.showSuccessMessage("Client created successfully! ID: " + newClient.getId());

        } catch (Exception e) {
            managerView.showError("Error creating client: " + e.getMessage());
        }
    }

    private void modifyClient() {
        try {
            String clientEmail = ConsoleUtils.readEmail("Enter client email");
            User client = bankingService.getUserByEmail(clientEmail);

            if (client == null || !client.getRole().equals(enums.Role.CLIENT)) {
                managerView.showError("Client not found.");
                return;
            }

            String firstName = ConsoleUtils.readString("New First Name");
            String lastName = ConsoleUtils.readString("New Last Name");
            String newEmail = ConsoleUtils.readEmail("New Email");
            String password = ConsoleUtils.readString("New Password");

            bankingService.updateUser(client.getId(), firstName, lastName, newEmail, password);
            managerView.showSuccessMessage("Client modified successfully!");

        } catch (Exception e) {
            managerView.showError("Error modifying client: " + e.getMessage());
        }
    }

    private void deleteClient() {
        try {
            String clientEmail = ConsoleUtils.readEmail("Enter client email");
            bankingService.deleteClient(clientEmail);
            managerView.showSuccessMessage("Client deleted successfully!");

        } catch (Exception e) {
            managerView.showError("Error deleting client: " + e.getMessage());
        }
    }

    private void createAccountForClient() {
        try {
            String clientEmail = ConsoleUtils.readEmail("Enter client email");
            User client = bankingService.getUserByEmail(clientEmail);

            if (client == null || !client.getRole().equals(enums.Role.CLIENT)) {
                managerView.showError("Client not found.");
                return;
            }

            AccountType accountType = managerView.selectAccountType();
            Account account = bankingService.createAccount(client.getId(), accountType);

            managerView.showSuccessMessage("Account created! ID: " + account.getAccountId());

        } catch (Exception e) {
            managerView.showError("Error creating account: " + e.getMessage());
        }
    }

    private void deleteClientAccount() {
        try {
            String accountId = ConsoleUtils.readString("Enter account ID");
            bankingService.deleteAccount(UUID.fromString(accountId));
            managerView.showSuccessMessage("Account deleted successfully!");

        } catch (Exception e) {
            managerView.showError("Error deleting account: " + e.getMessage());
        }
    }

    private void addTransaction() {
        try {
            String accountId = ConsoleUtils.readString("Enter account ID");
            TransactionType type = managerView.selectTransactionType();
            double amount = ConsoleUtils.readDouble("Enter amount");
            String description = ConsoleUtils.readString("Enter description");

            UUID accountUUID = UUID.fromString(accountId);

            switch (type) {
                case DEPOSIT:
                    bankingService.deposit(accountUUID, amount);
                    break;
                case WITHDRAWAL:
                    bankingService.withdraw(accountUUID, amount);
                    break;
                case TRANSFER:
                    String toAccountId = ConsoleUtils.readString("Enter destination account ID");
                    bankingService.transfer(accountUUID, UUID.fromString(toAccountId), amount);
                    break;
            }

            managerView.showSuccessMessage("Transaction added successfully!");

        } catch (Exception e) {
            managerView.showError("Error adding transaction: " + e.getMessage());
        }
    }

    private void logout() {
        bankingService.logout();
        managerView.showSuccessMessage("Logged out successfully.");
    }
}
