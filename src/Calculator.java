import java.util.Scanner;

import static java.lang.Float.NaN;

public class Calculator {
    public static float calc() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("First number: ");
        float num1 = Float.parseFloat(scanner.nextLine());

        System.out.print("Second number: ");
        float num2 = Float.parseFloat(scanner.nextLine());

        System.out.print("Operator (+ - * or /): ");
        String operation = scanner.nextLine();

        boolean error = false;

        float result = 0;

        switch (operation) {
            case "+": result = num1 + num2; break;
            case "-": result = num1 - num2; break;
            case "*": result = num1 * num2; break;
            case "/": result = num1 / num2; break;
            default: error = true;
        }

        if (error) {
            System.out.println("Error, wrong operator.");
            return NaN;
        } else {
            System.out.printf("%,.2f %s %,.2f = %,.2f\n", num1, operation, num2, result);
            return result;
        }
    }


}
