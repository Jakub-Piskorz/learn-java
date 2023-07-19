package com.example.demo;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
    Scanner scanner = new Scanner(System.in);

    System.out.println("\n\n\n");

    System.out.printf("HI MOM! Digit: %d, String: %s\n", 69, "420");

    System.out.printf("What's your name? ");
    String name = scanner.nextLine();

    // One way to deal with <enter> buffer after number.
    System.out.printf("What's your age? ");
    int age = scanner.nextInt();
    // cleans input buffer.
    scanner.nextLine();

    // The other way to deal with <enter> buffer after number.
    System.out.printf("How long is your hair? ");
    double hairSize = Double.parseDouble(scanner.nextLine());


    System.out.printf("Nice. Now I can steal your identity. How do you feel? ");
    String response = scanner.nextLine();

    System.out.println("Hi, my name is " + name + "and I'm " + age + " years old. I feel " + response + ".");
    System.out.printf("Hair size: %,.3f\n", hairSize);
    scanner.close();

    System.out.println("\n");
	}

}
