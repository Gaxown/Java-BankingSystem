package utils;


import java.util.Scanner;

public class Console {

    private final static Scanner SCANNER = new Scanner(System.in);

    public static String readLine(Scanner scanner) {
        String line = scanner.nextLine();
        // If line is empty (buffer issue), read again
        if (line.trim().isEmpty()) {
            line = scanner.nextLine();
        }
        return line;
    }

    public static void printHeader(String title) {
        System.out.println("===================================");
        System.out.println("       " + title);
        System.out.println("===================================");
    }

    public static void printFooter() {
        System.out.println("===================================");
    }

    public static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static boolean validatePassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordRegex);
    }


    public static boolean validateUserCredentials(String email, String password) {

        boolean isEmailValidated = Console.validatePassword(email);
        boolean isPasswordValidated = Console.validatePassword(password);

        return isEmailValidated && isPasswordValidated;
    }




    public static int showMenu(Scanner SCANNER, String title, String[] options) {
        printHeader(title);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.println("Choose an option: ");
        int choice = SCANNER.nextInt();
        printFooter();

        return choice;
    }

    public static void showWelcomeMenu() {
        printHeader("Welcome to Hotel Management System");

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        printFooter();
    }

    public static void askForUserInput() {
        int choice;

        System.out.print("Enter your choice: ");
        SCANNER.nextLine();
        choice = SCANNER.nextInt();
        switch (choice) {
            case 1 : System.out.println("Register");
            case 2 : System.out.println("Login");
            case 3 : {
                System.out.println("Exiting ...");
                System.exit(0);
            }
        }
    }



    // Sign In & Sign Up Forms

    public static void showLoginForm(Scanner SCANNER) {
        Console.printHeader("Login");

        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();

        Console.printFooter ();
    }

    public static void showRegisterForm(Scanner SCANNER) {
        Console.printHeader("Register");

        System.out.print("Full name: ");
        String fullName = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();

        Console.printFooter ();
    }

    public static void backToMainMenu(Scanner SCANNER) {
        int input = SCANNER.nextInt();

    }


    // helpers (input validatin)
    public static String validateStringInput(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return input.trim();
    }

    public static double validateDoubleInput(String input, String fieldName, double min, double max) {
        try {
            double value = Double.parseDouble(input.trim());
            if (value < min || value > max) {
                throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ". Please enter a valid number.");
        }
    }

    public static int validateIntInput(String input, String fieldName, int min, int max) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value < min || value > max) {
                throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ". Please enter a valid integer.");
        }
    }

    // General parsing with fallback
    public static double parseDouble(String input, double fallback, String fieldName, double min, double max) {
        try {
            return validateDoubleInput(input, fieldName, min, max);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + " Using previous value.");
            return fallback;
        }
    }

    public static int parseInt(String input, int fallback, String fieldName, int min, int max) {
        try {
            return validateIntInput(input, fieldName, min, max);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + " Using previous value.");
            return fallback;
        }
    }

    // Safe menu input method
    public static int getMenuChoice(Scanner scanner, int minChoice, int maxChoice) {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                String input = readLine(scanner);
                int choice = validateIntInput(input, "Menu choice", minChoice, maxChoice);
                return choice;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }



}


