package Models;

import enums.Role;

import java.util.List;

public class Client extends User {

    private List<Account> accounts;

    public Client(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, enums.Role.CLIENT);
    }

    public List<Account> getAccounts() {
            return accounts;
    }

    @Override
    public Role getRole() {
        return Role.CLIENT;
    }
}
