package Views;

import utils.ConsoleUtils;

public class WelcomeView {

    public void displayWelcomeMessage() {
        System.out.println("Welcome to The Banking System!");
    }

    public int displayWelcomeMenu() {
        String[] options = {
            "Login",
            "Register",
            "Exit"
        };

        ConsoleUtils.displayMenu("Main Menu", options);
        return ConsoleUtils.readInt("Choose an option");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String error) {
        System.out.println("Error: " + error);
    }
}
