package util;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SafeInput {
    
    private static final int INVALID_INPUT = -1;
    
    /**
     * Safely reads a line of input from the scanner
     * @param sc Scanner object
     * @return Input line or null if terminated
     */
    public static String getLine(Scanner sc) {
        try {
            return sc.nextLine();
        } catch (NoSuchElementException e) {
            handleInputTermination();
            return null;
        }
    }
    
    /**
     * Safely reads an integer from the scanner
     * @param sc Scanner object
     * @return Integer value or INVALID_INPUT (-1) if invalid
     */
    public static int getInt(Scanner sc) {
        try {
            return sc.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Invalid input. Please enter a number.");
            sc.next(); // Clear invalid input
            return INVALID_INPUT;
        } catch (NoSuchElementException e) {
            handleInputTermination();
            return INVALID_INPUT;
        }
    }
    
    /**
     * Handles input termination (Ctrl+Z)
     */
    private static void handleInputTermination() {
        System.err.println("Input terminated (Ctrl+Z detected). Exiting...");
        System.exit(0);
    }
}