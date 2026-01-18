package com.banking.banking.model;



import java.util.ArrayList;
import java.util.List;

public class User {
    private int accountNumber;
    private String fullName;
    private String email;
    private String password;
    private Account account;
    private List<Transaction> transactions;

    public User() {
        this.transactions = new ArrayList<>();
    }

    public User(int accountNumber, String fullName, String email, String password) {
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.account = new Account(accountNumber, 0.0);
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}