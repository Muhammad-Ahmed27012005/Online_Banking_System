package com.bank.model;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;
    private Integer fromAccount;
    private Integer toAccount;

    // Constructor
    public Transaction(TransactionType type, double amount,
            Integer fromAccount, Integer toAccount) {
        this.transactionId = generateTransactionId();
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.description = generateDescription();
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }

    private String generateDescription() {
        switch (type) {
            case DEPOSIT:
                return "Deposit of $" + amount;
            case WITHDRAWAL:
                return "Withdrawal of $" + amount;
            case TRANSFER:
                return "Transfer of $" + amount + " to Account: " + toAccount;
            default:
                return "Transaction";
        }
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public TransactionType getType() {
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

    public Integer getFromAccount() {
        return fromAccount;
    }

    public Integer getToAccount() {
        return toAccount;
    }
}