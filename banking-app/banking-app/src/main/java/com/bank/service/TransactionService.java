package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private BankRepository bankRepository;

    // Store transaction history
    private Map<String, Transaction> transactionHistory = new HashMap<>();

    public Map<String, Object> deposit(Integer accountNumber, double amount) {
        Map<String, Object> response = new HashMap<>();

        Account account = bankRepository.getAccount(accountNumber);
        if (account == null) {
            response.put("success", false);
            response.put("message", "Account not found");
            return response;
        }

        if (amount <= 0) {
            response.put("success", false);
            response.put("message", "Amount must be greater than 0");
            return response;
        }

        boolean success = account.deposit(amount);
        if (success) {
            Transaction transaction = new Transaction(TransactionType.DEPOSIT,
                    amount, accountNumber, null);
            transactionHistory.put(transaction.getTransactionId(), transaction);

            response.put("success", true);
            response.put("message", "Deposit successful");
            response.put("newBalance", account.getBalance());
            response.put("transaction", transaction);
        } else {
            response.put("success", false);
            response.put("message", "Deposit failed");
        }

        return response;
    }

    public Map<String, Object> withdraw(Integer accountNumber, double amount) {
        Map<String, Object> response = new HashMap<>();

        Account account = bankRepository.getAccount(accountNumber);
        if (account == null) {
            response.put("success", false);
            response.put("message", "Account not found");
            return response;
        }

        if (amount <= 0) {
            response.put("success", false);
            response.put("message", "Amount must be greater than 0");
            return response;
        }

        boolean success = account.withdraw(amount);
        if (success) {
            Transaction transaction = new Transaction(TransactionType.WITHDRAWAL,
                    amount, accountNumber, null);
            transactionHistory.put(transaction.getTransactionId(), transaction);

            response.put("success", true);
            response.put("message", "Withdrawal successful");
            response.put("newBalance", account.getBalance());
            response.put("transaction", transaction);
        } else {
            response.put("success", false);
            response.put("message", "Insufficient balance or invalid amount");
        }

        return response;
    }

    public Map<String, Object> transfer(Integer fromAccount, Integer toAccount, double amount) {
        Map<String, Object> response = new HashMap<>();

        // Validate sender account
        Account sender = bankRepository.getAccount(fromAccount);
        if (sender == null) {
            response.put("success", false);
            response.put("message", "Sender account not found");
            return response;
        }

        // Validate receiver account
        if (!bankRepository.accountExists(toAccount)) {
            response.put("success", false);
            response.put("message", "Receiver account not found");
            return response;
        }

        Account receiver = bankRepository.getAccount(toAccount);

        if (amount <= 0) {
            response.put("success", false);
            response.put("message", "Amount must be greater than 0");
            return response;
        }

        // Check sufficient balance
        if (sender.getBalance() < amount) {
            response.put("success", false);
            response.put("message", "Insufficient balance for transfer");
            return response;
        }

        // Perform transfer
        sender.withdraw(amount);
        receiver.deposit(amount);

        // Record transaction
        Transaction transaction = new Transaction(TransactionType.TRANSFER,
                amount, fromAccount, toAccount);
        transactionHistory.put(transaction.getTransactionId(), transaction);

        response.put("success", true);
        response.put("message", "Transfer successful");
        response.put("newBalance", sender.getBalance());
        response.put("transaction", transaction);

        return response;
    }

    public Map<String, Object> getAccountInfo(Integer accountNumber) {
        Map<String, Object> response = new HashMap<>();

        Account account = bankRepository.getAccount(accountNumber);
        if (account != null) {
            response.put("success", true);
            response.put("accountNumber", account.getAccountNumber());
            response.put("accountHolder", account.getAccountHolderName());
            response.put("balance", account.getBalance());
            response.put("transactionHistory", account.getTransactionHistory());
        } else {
            response.put("success", false);
            response.put("message", "Account not found");
        }

        return response;
    }
}