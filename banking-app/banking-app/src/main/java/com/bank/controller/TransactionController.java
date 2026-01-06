package com.bank.controller;

import com.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Default account for demo (you can extend this to support multiple users)
    private static final Integer DEFAULT_ACCOUNT_NUMBER = 1001;

    @GetMapping("/")
    public String showTransactionPage(Model model) {
        // Get account info for display
        Map<String, Object> accountInfo = transactionService.getAccountInfo(DEFAULT_ACCOUNT_NUMBER);

        if ((Boolean) accountInfo.get("success")) {
            model.addAttribute("accountNumber", accountInfo.get("accountNumber"));
            model.addAttribute("accountHolder", accountInfo.get("accountHolder"));
            model.addAttribute("balance", accountInfo.get("balance"));
        }

        model.addAttribute("transactionType", "DEPOSIT");
        return "index";
    }

    @PostMapping("/deposit")
    @ResponseBody
    public Map<String, Object> deposit(@RequestParam double amount) {
        return transactionService.deposit(DEFAULT_ACCOUNT_NUMBER, amount);
    }

    @PostMapping("/withdraw")
    @ResponseBody
    public Map<String, Object> withdraw(@RequestParam double amount) {
        return transactionService.withdraw(DEFAULT_ACCOUNT_NUMBER, amount);
    }

    @PostMapping("/transfer")
    @ResponseBody
    public Map<String, Object> transfer(@RequestParam double amount,
            @RequestParam Integer toAccount) {
        return transactionService.transfer(DEFAULT_ACCOUNT_NUMBER, toAccount, amount);
    }

    @GetMapping("/account")
    @ResponseBody
    public Map<String, Object> getAccountInfo() {
        return transactionService.getAccountInfo(DEFAULT_ACCOUNT_NUMBER);
    }
}