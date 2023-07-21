import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char again = 'y';
        LinkedList<Float> results = new LinkedList<>();
        while (again == 'y') {
            System.out.flush();
            results.add(Calculator.calc());

            System.out.print("Wanna do it again? (y/n) ");
            again = scanner.next().charAt(0);
        }
        System.out.println("List of previous results: " + results);
    }
}