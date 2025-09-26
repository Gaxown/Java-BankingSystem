package Controllers;

import Models.Manager;
import Services.BankingService;
import Views.WelcomeView;
import enums.Role;
import utils.ConsoleUtils;

public class MainController {
    private BankingService bankingService = BankingService.getInstance();
    private WelcomeView welcomeView = new WelcomeView();
    private ClientController clientController = new ClientController();
    private ManagerController managerController = new ManagerController();

    public void start() {
        welcomeView.displayWelcomeMessage();

        while (true) {
            if (bankingService.user() != null) {
                handleUserMenu();
            } else {
                handleWelcomeMenu();
            }
        }
    }

    private void handleWelcomeMenu() {
        int choice = welcomeView.displayWelcomeMenu();

        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegister();
                break;
            case 3:
                welcomeView.showMessage("Exiting the system. Goodbye!");
                System.exit(0);
                break;
            default:
                welcomeView.showError("Invalid choice. Please try again.");
        }
    }

    private void handleUserMenu() {
        if (bankingService.user().getRole() == Role.CLIENT) {
            clientController.handleClientMenu();
        } else {
            managerController.handleManagerMenu();
        }
    }

    private void handleLogin() {
        try {
            String email = ConsoleUtils.readEmail("Email");
            String password = ConsoleUtils.readString("Password");

            bankingService.login(email, password);
            welcomeView.showMessage("Login successful!");

        } catch (Exception ex) {
            welcomeView.showError("Login failed: " + ex.getMessage());
        }
    }

    private void handleRegister() {
        welcomeView.showMessage("Registering a new manager...");

        try {
            String firstName = ConsoleUtils.readString("First Name");
            String lastName = ConsoleUtils.readString("Last Name");
            String email = ConsoleUtils.readEmail("Email");
            String password = ConsoleUtils.readString("Password");

            bankingService.createManager(firstName, lastName, email, password, "IT");
            welcomeView.showMessage("Registration successful! You can now log in.");

        } catch (Exception ex) {
            welcomeView.showError("Error during registration: " + ex.getMessage());
        }
    }
}
