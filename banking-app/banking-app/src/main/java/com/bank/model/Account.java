package com.bank.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private Integer accountNumber;
    private String accountHolderName;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(Integer accountNumber, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = 0.0; // Initial balance is 0
        this.transactionHistory = new ArrayList<>();
    }

    // Deposit method
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        Transaction transaction = new Transaction(TransactionType.DEPOSIT,
                amount, this.accountNumber, null);
        transactionHistory.add(transaction);
        return true;
    }

    // Withdraw method
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        this.balance -= amount;
        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL,
                amount, this.accountNumber, null);
        transactionHistory.add(transaction);
        return true;
    }

    // Getters
    public Integer getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}