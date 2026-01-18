package com.banking.banking;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		System.out.println("Online Banking System Started!");
		System.out.println("Open: http://localhost:8080");
	}
}