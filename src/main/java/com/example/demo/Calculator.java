package com.example.demo;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Calculator {

	public static void main(String[] args) {
		SpringApplication.run(Calculator.class, args);
    Scanner scanner = new Scanner(System.in);

    System.out.println("\n\n\n");

    System.out.println("CALCULATOR!!!");

    System.out.println("Enter first number!");
    double num1 = Double.parseDouble(scanner.nextLine());

    System.out.println("Enter second number!");
    double num2 = Double.parseDouble(scanner.nextLine());

    scanner.close();

    System.out.printf("%,.2f %,.2f", num1, num2);
	}

}
