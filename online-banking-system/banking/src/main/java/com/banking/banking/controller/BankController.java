package com.banking.banking.controller;

import com.banking.banking.model.User;
import com.banking.banking.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BankController {

    @Autowired
    private BankService bankService;

    // Home Page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Login Page
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) Integer accountNumber,
            Model model) {
        if (accountNumber != null) {
            model.addAttribute("accountNumber", accountNumber);
        }
        return "login";
    }

    // Login Processing
    @PostMapping("/login")
    public String login(@RequestParam int accountNumber,
            @RequestParam String password,
            Model model) {
        User user = bankService.login(accountNumber, password);

        if (user != null) {
            model.addAttribute("user", user);
            return "dashboard";
        } else {
            model.addAttribute("error", "Invalid account number or password!");
            model.addAttribute("accountNumber", accountNumber);
            return "login";
        }
    }

    // Signup Page
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // Signup Processing
    @PostMapping("/signup")
    public String signup(@RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam double initialDeposit,
            Model model) {

        BankService.RegistrationResult result = bankService.registerUser(
                fullName, email, password, initialDeposit);

        if (result.isSuccess()) {
            model.addAttribute("accountNumber", result.getAccountNumber());
            model.addAttribute("fullName", fullName);
            model.addAttribute("initialDeposit", initialDeposit);
            model.addAttribute("message", result.getMessage());
            return "registration-success";
        } else {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "signup";
        }
    }

    // Registration Success Page
    @GetMapping("/registration-success")
    public String registrationSuccess(@RequestParam int accountNumber,
            @RequestParam String fullName,
            @RequestParam double initialDeposit,
            Model model) {
        model.addAttribute("accountNumber", accountNumber);
        model.addAttribute("fullName", fullName);
        model.addAttribute("initialDeposit", initialDeposit);
        return "registration-success";
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dashboard";
    }

    // Deposit Page
    @GetMapping("/deposit")
    public String depositPage(Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "deposit";
    }

    // Process Deposit
    @PostMapping("/deposit")
    public String processDeposit(@RequestParam double amount, Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        if (amount <= 0) {
            model.addAttribute("error", "Amount must be positive!");
            model.addAttribute("user", user);
            return "deposit";
        }

        boolean success = bankService.deposit(amount);
        if (success) {
            model.addAttribute("success", "Deposit successful!");
        } else {
            model.addAttribute("error", "Deposit failed!");
        }

        model.addAttribute("user", bankService.getCurrentUser());
        return "dashboard";
    }

    // Withdraw Page
    @GetMapping("/withdraw")
    public String withdrawPage(Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "withdraw";
    }

    // Process Withdrawal
    @PostMapping("/withdraw")
    public String processWithdraw(@RequestParam double amount, Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        if (amount <= 0) {
            model.addAttribute("error", "Amount must be positive!");
            model.addAttribute("user", user);
            return "withdraw";
        }

        boolean success = bankService.withdraw(amount);
        if (success) {
            model.addAttribute("success", "Withdrawal successful!");
        } else {
            model.addAttribute("error", "Insufficient balance!");
            model.addAttribute("user", user);
            return "withdraw";
        }

        model.addAttribute("user", bankService.getCurrentUser());
        return "dashboard";
    }

    // Transfer Page
    @GetMapping("/transfer")
    public String transferPage(Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "transfer";
    }

    // Process Transfer
    @PostMapping("/transfer")
    public String processTransfer(@RequestParam int toAccount,
            @RequestParam double amount,
            Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        if (amount <= 0) {
            model.addAttribute("error", "Amount must be positive!");
            model.addAttribute("user", user);
            return "transfer";
        }

        boolean success = bankService.transfer(toAccount, amount);
        if (success) {
            model.addAttribute("success", "Transfer successful!");
        } else {
            model.addAttribute("error", "Transfer failed! Check account number or balance.");
            model.addAttribute("user", user);
            return "transfer";
        }

        model.addAttribute("user", bankService.getCurrentUser());
        return "dashboard";
    }

    // Transactions Page
    @GetMapping("/transactions")
    public String transactionsPage(Model model) {
        User user = bankService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("transactions", user.getTransactions());
        return "transactions";
    }

    // Logout
    @GetMapping("/logout")
    public String logout() {
        bankService.logout();
        return "redirect:/login";
    }
}