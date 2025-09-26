package Models;

import enums.TransactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Transaction {
    private UUID transactionId;
    private UUID accountId;
    private double amount;
    private TransactionType transactionType;
    private String timestamp;
    private String description;

    public static final List<Transaction> transactions = new ArrayList<>();


    public Transaction(UUID transactionId, UUID accountId, double amount, TransactionType transactionType, String timestamp, String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.description = description;
    }


    public static void save(Transaction transaction) {
        transactions.add(transaction);
    }


    public static List<Transaction> getAll() {
        return transactions;
    }


    public static List<Transaction> getByAccountId(UUID accountId) {
        return transactions.stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId)).collect(Collectors.toList());
    }

    public static List<Transaction> getByClientId(UUID clientId) {
        return transactions.stream().filter(transaction -> transaction.getAccountId().equals(clientId)).collect(Collectors.toList());
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
