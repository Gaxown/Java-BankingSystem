package Services;

import Models.*;
import enums.Role;
import enums.TransactionType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class BankingService {

    private static BankingService instance = null;
    private User currentUser = null;
    // njrebo singelton o sf

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

        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || password == null || password.isEmpty())
            throw new IllegalArgumentException("All fields must be filled");

        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (email != null && !email.isEmpty()) {
            if (!email.equalsIgnoreCase(user.getEmail()) && User.getByEmail(email).isPresent())
                throw new IllegalArgumentException("Email already in use");
            user.setEmail(email);
        }

        user.setPassword(password);
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

        Account account = new Account(accountType);

        // Link account to client
        Client client = (Client) user;
        if (client.getAccounts() == null) {
            client.setAccounts(new ArrayList<>());
        }
        client.getAccounts().add(account);

        Account.save(account);
        return account;
    }

    public void deposit(UUID accountId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }

        Optional<Account> accountOpt = Account.getById(accountId);
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            accountId,
            amount,
            TransactionType.DEPOSIT,
            LocalDateTime.now().toString(),
            "Deposit to account"
        );
        Transaction.save(transaction);
    }

    public void withdraw(UUID accountId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0");
        }

        Optional<Account> accountOpt = Account.getById(accountId);
        if (!accountOpt.isPresent()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds. Current balance: $" +
                String.format("%.2f", account.getBalance()));
        }

        account.setBalance(account.getBalance() - amount);

        // Create transaction record
        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            accountId,
            amount,
            TransactionType.WITHDRAWAL,
            LocalDateTime.now().toString(),
            "Withdrawal from account"
        );
        Transaction.save(transaction);
    }

    public void transfer(UUID fromAccountId, UUID toAccountId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than 0");
        }

        Optional<Account> fromAccountOpt = Account.getById(fromAccountId);
        Optional<Account> toAccountOpt = Account.getById(toAccountId);

        if (!fromAccountOpt.isPresent()) {
            throw new IllegalArgumentException("Source account not found");
        }
        if (!toAccountOpt.isPresent()) {
            throw new IllegalArgumentException("Destination account not found");
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds in source account. Current balance: $" +
                String.format("%.2f", fromAccount.getBalance()));
        }

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        Transaction debitTransaction = new Transaction(
            UUID.randomUUID(),
            fromAccountId,
            amount,
            TransactionType.TRANSFER,
            LocalDateTime.now().toString(),
            "Transfer to account " + toAccountId
        );

        Transaction creditTransaction = new Transaction(
            UUID.randomUUID(),
            toAccountId,
            amount,
            TransactionType.TRANSFER,
            LocalDateTime.now().toString(),
            "Transfer from account " + fromAccountId
        );

        Transaction.save(debitTransaction);
        Transaction.save(creditTransaction);
    }

    // Manager Methods
    public List<User> getAllUsers() {
        return new ArrayList<>(User.users);
    }

    public List<Transaction> getAllTransactions() {
        return Transaction.getAll();
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(Account.accounts);
    }

    public User getUserByEmail(String email) {
        Optional<User> userOpt = User.getByEmail(email);
        return userOpt.orElse(null);
    }

    public Account getAccountById(UUID accountId) {
        Optional<Account> accountOpt = Account.getById(accountId);
        return accountOpt.orElse(null);
    }

    public List<Transaction> getAllTransactionsForClient(UUID clientId) {
        Client client = (Client) User.getById(clientId).orElseThrow(() ->
            new NoSuchElementException("Client not found"));

        return client.getAccounts().stream()
                .flatMap(account -> Transaction.getByAccountId(account.getAccountId()).stream())
                .collect(java.util.stream.Collectors.toList());
    }

    public double calculateTotalBalance(UUID clientId) {
        Client client = (Client) User.getById(clientId).orElseThrow(() ->
            new NoSuchElementException("Client not found"));

        return client.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public double calculateTotalDeposits(UUID clientId) {
        return getAllTransactionsForClient(clientId).stream()
                .filter(t -> t.getTransactionType() == TransactionType.DEPOSIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateTotalWithdrawals(UUID clientId) {
        return getAllTransactionsForClient(clientId).stream()
                .filter(t -> t.getTransactionType() == TransactionType.WITHDRAWAL)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    //   Managing cliens
    public void deleteClient(String email) {
        User user = getUserByEmail(email);
        if (user == null || user.getRole() != Role.CLIENT) {
            throw new NoSuchElementException("Client not found");
        }
        User.users.remove(user);
    }

    public void deleteAccount(UUID accountId) {
        Account account = Account.getById(accountId).orElseThrow(() ->
            new NoSuchElementException("Account not found"));
        Account.accounts.remove(account);

        User.users.stream()
                .filter(u -> u instanceof Client)
                .map(u -> (Client) u)
                .forEach(c -> c.getAccounts().removeIf(a -> a.getAccountId().equals(accountId)));
    }

}
