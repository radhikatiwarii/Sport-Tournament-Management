package util;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SafeInput {
    
    private static final int INVALID_INPUT = -1;
    
    // Use UniversalInput for all string inputs
    public static String getLine(Scanner sc) {
        return UniversalInput.getInput(sc, "");
    }
    
    public static String getLineWithPrompt(Scanner sc, String prompt) {
        return UniversalInput.getInput(sc, prompt);
    }
    
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
    
    private static void handleInputTermination() {
        System.err.println("Input terminated (Ctrl+Z detected). Exiting...");
        System.exit(0);
    }
}