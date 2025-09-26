package Controllers;

public class ClientController implements UserController {



    @Override
    public boolean authenticate(String email, String password) {

        return false;
    }

    @Override
    public void logout() {
        // Implementation for client logout
    }

    @Override
    public void viewProfile() {
        // Implementation for viewing client profile
    }

    @Override
    public void updateProfile() {
        // Implementation for updating client profile
    }

    @Override
    public void changePassword(String newPassword) {
        // Implementation for changing client password
    }

    @Override
    public static void displayMenu() {
        // Implementation for displaying client menu
    }

    @Override
    public void viewTransactions() {
        // Implementation for viewing client transactions
    }

    @Override
    public void performAction(int actionCode) {
        // Implementation for performing client-specific actions based on actionCode
    }

}
