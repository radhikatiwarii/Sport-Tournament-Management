package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputUtil {
    public static int chooseInt(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }
    }
}
