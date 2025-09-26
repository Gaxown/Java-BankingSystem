package Models;

import enums.Role;

import java.util.ArrayList;
import java.util.List;

public class Client extends User {

    private List<Account> accounts;

    public Client(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, enums.Role.CLIENT);
        this.accounts = new ArrayList<>();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Role getRole() {
        return Role.CLIENT;
    }
}
