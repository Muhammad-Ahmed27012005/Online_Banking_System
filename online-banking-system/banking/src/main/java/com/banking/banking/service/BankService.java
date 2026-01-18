package com.banking.banking.service;


import com.banking.banking.model.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BankService {
    // HashMap for O(1) user lookup by account number
    private Map<Integer, User> users = new HashMap<>();
    private User currentUser = null;

    // Track the last generated account number
    private AtomicInteger lastAccountNumber = new AtomicInteger(100000);

    // Store registration data temporarily
    private User tempRegisteredUser = null;

    public BankService() {
        // Add some sample users for testing
        initializeSampleData();
    }

    private void initializeSampleData() {
        int accNo1 = generateAccountNumber();
        User user1 = new User(accNo1, "John Doe", "john@example.com", "password123");
        user1.getAccount().deposit(1000);
        users.put(accNo1, user1);

        int accNo2 = generateAccountNumber();
        User user2 = new User(accNo2, "Jane Smith", "jane@example.com", "password456");
        user2.getAccount().deposit(500);
        users.put(accNo2, user2);
    }

    public int generateAccountNumber() {
        // Generate a unique 6-digit account number
        int accountNumber;
        do {
            accountNumber = lastAccountNumber.incrementAndGet();
        } while (users.containsKey(accountNumber));

        return accountNumber;
    }

    public RegistrationResult registerUser(String fullName, String email, String password, double initialDeposit) {
        int accountNumber = generateAccountNumber();

        User newUser = new User(accountNumber, fullName, email, password);
        if (initialDeposit > 0) {
            newUser.getAccount().deposit(initialDeposit);
            Transaction deposit = new Transaction(accountNumber, "DEPOSIT", initialDeposit,
                    "Initial Deposit");
            newUser.addTransaction(deposit);
        }

        users.put(accountNumber, newUser);

        // Store temporarily for display
        tempRegisteredUser = newUser;

        return new RegistrationResult(true, accountNumber, "Registration Successful!");
    }

    public User getTempRegisteredUser() {
        return tempRegisteredUser;
    }

    public void clearTempRegisteredUser() {
        tempRegisteredUser = null;
    }

    public User login(int accountNumber, String password) {
        User user = users.get(accountNumber);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean deposit(double amount) {
        if (currentUser == null || amount <= 0) {
            return false;
        }

        currentUser.getAccount().deposit(amount);
        Transaction transaction = new Transaction(
                currentUser.getAccountNumber(),
                "DEPOSIT",
                amount,
                "Cash Deposit");
        currentUser.addTransaction(transaction);
        return true;
    }

    public boolean withdraw(double amount) {
        if (currentUser == null || amount <= 0) {
            return false;
        }

        boolean success = currentUser.getAccount().withdraw(amount);
        if (success) {
            Transaction transaction = new Transaction(
                    currentUser.getAccountNumber(),
                    "WITHDRAW",
                    amount,
                    "Cash Withdrawal");
            currentUser.addTransaction(transaction);
        }
        return success;
    }

    public boolean transfer(int toAccountNumber, double amount) {
        if (currentUser == null || amount <= 0) {
            return false;
        }

        User receiver = users.get(toAccountNumber);
        if (receiver == null) {
            return false;
        }

        // Withdraw from sender
        boolean withdrawalSuccess = currentUser.getAccount().withdraw(amount);
        if (!withdrawalSuccess) {
            return false;
        }

        // Deposit to receiver
        receiver.getAccount().deposit(amount);

        // Record transactions for both users
        Transaction senderTransaction = new Transaction(
                currentUser.getAccountNumber(),
                "TRANSFER",
                amount,
                "Transfer to Account: " + toAccountNumber);
        currentUser.addTransaction(senderTransaction);

        Transaction receiverTransaction = new Transaction(
                toAccountNumber,
                "TRANSFER",
                amount,
                "Transfer from Account: " + currentUser.getAccountNumber());
        receiver.addTransaction(receiverTransaction);

        return true;
    }

    public Map<Integer, User> getAllUsers() {
        return new HashMap<>(users);
    }

    public void logout() {
        currentUser = null;
    }

    // Inner class to store registration result
    public static class RegistrationResult {
        private boolean success;
        private int accountNumber;
        private String message;

        public RegistrationResult(boolean success, int accountNumber, String message) {
            this.success = success;
            this.accountNumber = accountNumber;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public String getMessage() {
            return message;
        }
    }
}