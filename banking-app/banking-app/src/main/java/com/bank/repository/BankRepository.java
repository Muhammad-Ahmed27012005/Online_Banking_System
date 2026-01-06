package com.bank.repository;

import com.bank.model.Account;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BankRepository {
    private Map<Integer, Account> accounts = new HashMap<>();
    private Integer currentAccountNumber = 1001; // Starting account number

    public BankRepository() {
        // Initialize with some demo accounts
        createAccount("John Doe");
        createAccount("Jane Smith");
    }

    public Account createAccount(String accountHolderName) {
        Account account = new Account(currentAccountNumber, accountHolderName);
        accounts.put(currentAccountNumber, account);
        currentAccountNumber++;
        return account;
    }

    public Account getAccount(Integer accountNumber) {
        return accounts.get(accountNumber);
    }

    public Map<Integer, Account> getAllAccounts() {
        return new HashMap<>(accounts);
    }

    public boolean accountExists(Integer accountNumber) {
        return accounts.containsKey(accountNumber);
    }
}