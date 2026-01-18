package com.banking.banking.model;



import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int accountNumber;
    private String type; // DEPOSIT, WITHDRAW, TRANSFER
    private double amount;
    private LocalDateTime timestamp;
    private String description;

    // Static counter for transaction IDs
    private static int idCounter = 1000;

    public Transaction() {
        this.transactionId = idCounter++;
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(int accountNumber, String type, double amount, String description) {
        this();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }
}