package Models;

import enums.Role;

import java.util.*;
import java.util.stream.Collectors;

public abstract class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    public static final List<User> users = new ArrayList<>();


    public User(String firstName, String lastName, String email, String password, Role role) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Optional<User> getById(UUID id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public static Optional<User> getByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public static void save(User user) {
        users.add(user);
    }


    public List<Transaction> getTransactions() {
        return Transaction.getAll().stream().filter(transaction -> transaction.getAccountId().equals(this.id)).collect(Collectors.toList());
    }

    public UUID getId() { return id; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public abstract Role getRole();
}
