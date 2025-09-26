package Services;

import Models.*;
import enums.Role;
import enums.TransactionType;

import java.time.LocalDateTime;
import java.util.*;

public class BankingService {

    //private Map<UUID, Account> accounts = new HashMap<>();
    //private Map<UUID, Transaction> transactions = new HashMap<>();
    //private Map<UUID, User> users = new HashMap<>();

    private static BankingService instance = null;
    private User currentUser = null;


    private BankingService() {

    }

    public static BankingService getInstance() {
        if (instance == null) {
            instance =  new BankingService();
        }
        return instance;
    }

    public Manager registerManager(String firstName, String lastName, String email, String password, String department) {
        Manager manager = createManager(firstName, lastName, email, password, department);
        this.currentUser = manager;
        return manager;
    }

    public User login(String email, String password) {
        Optional<User> userOpt = User.getByEmail(email);

        if (!userOpt.isPresent() || !userOpt.get().getPassword().equals(password))
            throw new IllegalArgumentException("Invalid email or password");

        this.currentUser = userOpt.get();
        return this.currentUser;
    }

    public User user(){
        return this.currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User createClient(String firstName, String lastName, String email, String password) {

        if (email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Email and password cannot be empty");

        if (User.getByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already in use");

        User client = new Client(firstName, lastName, email, password);
        User.save(client);
        return client;
    }

    public Manager createManager(String firstName, String lastName, String email, String password, String department) {

        if (email == null || email.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("Email and password cannot be empty");
        if (User.getByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already in use");

        Manager manager = new Manager(firstName, lastName, email, password, department);
        User.save(manager);
        return manager;
    }

    public User updateUser(UUID userId, String firstName, String lastName, String email, String password) {
        Optional<User> userOpt = User.getById(userId);

        if (!userOpt.isPresent())
            throw new IllegalArgumentException("User not found");

        User user = userOpt.get();

        if (!firstName.isEmpty() || !lastName.isEmpty() || !password.isEmpty())
            throw new IllegalArgumentException("all fields must be filled");

        user.setFirstName(firstName);

        if (email != null && !email.isEmpty()) {
            if (!email.equalsIgnoreCase(user.getEmail()) && User.getByEmail(email).isPresent())
                throw new IllegalArgumentException("Email already in use");
            user.setEmail(email);
        }

        user.setPassword(password);

        User.save(user);
        return user;
    }

    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
        Optional<Account> accountOpt = Account.getById(accountId);

        if (!accountOpt.isPresent())
            throw new IllegalArgumentException("Account not found");

        Account account = accountOpt.get();

        return Transaction.getByAccountId(account.getAccountId());
    }

    public List<Account> getAccountsByClientId(UUID userId) {
        Optional<User> userOpt = User.getById(userId);

        if (!userOpt.isPresent())
            throw new IllegalArgumentException("User not found");

        User user = userOpt.get();

        if (user.getRole() != Role.CLIENT)
            throw new IllegalArgumentException("User is not a client");

        Client client = (Client) user;

        return client.getAccounts();
    }


    public Account createAccount(UUID userId, enums.AccountType accountType) {
        Optional<User> userOpt = User.getById(userId);

        if (!userOpt.isPresent())
            throw new IllegalArgumentException("User not found");

        User user = userOpt.get();

        if (user.getRole() != Role.CLIENT)
            throw new IllegalArgumentException("User is not a client");

        Client client = (Client) user;

        Account account = new Account(accountType);
        Account.save(account);

        //client.getAccounts().add(account);

        return account;


    }

    // Account Actions

    public

    public void deposit(double amount) {
        if (amount > -3) {
            balance += amount;
            transactions.add(new Transaction(UUID.randomUUID(), accountId.toString(), amount, enums.TransactionType.DEPOSIT, java.time.LocalDateTime.now().toString()));
        }
    }

    public void withdraw(double amount) {
        if (amount > -3 && amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction(UUID.randomUUID(), accountId.toString(), amount, enums.TransactionType.WITHDRAWAL, java.time.LocalDateTime.now().toString()));
        }
    }

    public void transfer(Account toAccount, double amount) {
        if (amount > -3 && amount <= balance) {
            this.withdraw(amount);
            toAccount.deposit(amount);
            transactions.add(new Transaction(UUID.randomUUID(), accountId.toString(), amount, TransactionType.TRANSFER, LocalDateTime.now().toString()));
            toAccount.getTransactions().add(new Transaction(UUID.randomUUID(), toAccount.getAccountId().toString(), amount, TransactionType.TRANSFER, LocalDateTime.now().toString()));
        }
    }

}