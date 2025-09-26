package Models;


import enums.AccountType;
import enums.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Account {
    private UUID accountId;
    private AccountType accountType;
    private double balance;
    private UUID clientId;



    public static final ArrayList<Account> accounts = new ArrayList<>();

    public List<Transaction> getTransactionByAccount() {
        return Transaction.getByAccountId(this.accountId);
    }

    public List<Transaction> getTransactionsByClient(UUID clientId) {
        return Transaction.getByClientId(clientId);
    }

    public Account(AccountType accountType) {
        this.accountId = UUID.randomUUID();
        this.accountType = accountType;
        this.balance = 0.0;
    }

    public static void save(Account account) {
        accounts.add(account);
    }


    public static Optional<Account> getById(UUID accountId) {
        return accounts.stream()
                .filter(account -> account.getAccountId().equals(accountId))
                .findFirst();
    }


    public UUID getAccountId() {
        return accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }



}