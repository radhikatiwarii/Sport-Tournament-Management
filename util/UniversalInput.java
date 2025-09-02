package util;

import java.util.Scanner;
import java.util.Stack;

public class UniversalInput {
    private static Stack<Runnable> navigationStack = new Stack<>();
    
    // Universal input method - use this everywhere
    public static String getInput(Scanner sc, String prompt) {
        System.out.print(prompt);
        String input = sc.nextLine();
        
        if (input != null && "back".equalsIgnoreCase(input.trim())) {
            goBack();
            return null;
        }
        
        return input;
    }
    
    public static String getInputTrim(Scanner sc, String prompt) {
        String input = getInput(sc, prompt);
        return input != null ? input.trim() : null;
    }
    
    public static void pushStep(Runnable step) {
        navigationStack.push(step);
    }
    
    private static void goBack() {
        if (navigationStack.size() > 1) {
            navigationStack.pop(); // Remove current step
            // Don't run previous step automatically
            // Just signal back was pressed
        }
    }
    
    public static void clearStack() {
        navigationStack.clear();
    }
    
    public static boolean isFirstStep() {
        return navigationStack.size() <= 1;
    }
}