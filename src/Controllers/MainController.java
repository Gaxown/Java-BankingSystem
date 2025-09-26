package Controllers;

import Models.Manager;
import Services.BankingService;
import enums.Role;
import utils.Console;
import utils.ConsoleUtils;

public class MainController {
    BankingService bankingService = BankingService.getInstance();
    public void start() {
        System.out.println("Welcome to The banking System!");


        boolean running = true;


        while (true) {
            // showMainMenu();
            if (bankingService.user() != null){
                handleUserMenu();
            } else {
                handleWelcomeMenu();
            }
        }
    }

    private void handleWelcomeMenu(){
        int choice = 0;
        switch (choice) {
            case 1:
                // handleLogin();
                break;
            case 2:
                handleRegister();
                break;
            case 3:
                System.out.println("Exiting the system. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private  void  handleUserMenu(){
        if (bankingService.user().getRole() == Role.CLIENT){*
            handleClientMenu();
        } else {
           handleManagerMenu();
        }
    }

    private void handleClientMenu() {
        while (true) {
            ClientController.displayMenu();
            int choice = ConsoleUtils.readInt("Choose an option");

            switch (choice) {
                case 1:
                    // View Profile
                    ClientController.viewProfile(bankingService.user());
                    break;
                case 2:
                    // Update Profile
                    String fname = ConsoleUtils.readString("First Name");
                    String lname = ConsoleUtils.readString("Last Name");
                    String email = ConsoleUtils.readEmail("Email");
                    String password = ConsoleUtils.readString("Password");
                    ClientController.updateProfile(bankingService.user(), fname, lname, email, password);
                    break;
                case 3:
                    // Change Password
                    break;
                case 4:
                    // View Transactions
                    break;
                case 5:
                    // Logout
                    bankingService.logout();
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }

    private  void handleManagerMenu() {

    }

    private void handleRegister() {
        // Implement login logic
        System.out.println("Registering a new user...");

        while (true) {
            try {
                String firstName = ConsoleUtils.readString("First Name");
                String lastName = ConsoleUtils.readString("Last Name");
                String email = ConsoleUtils.readEmail("Email");
                String password = ConsoleUtils.readString("Password");

                Manager manager = bankingService.createManager(firstName, lastName, email, password, "IT");

                System.out.println("Registration successful! You can now log in.");
                return;

            } catch (Exception ex) {
                System.out.println("Error during registration: " + ex.getMessage());
                return;
            }
        }
    }


}
