package util;

import java.util.Scanner;
import java.util.Stack;

public class GlobalInputHandler {
    private static Stack<Runnable> navigationStack = new Stack<>();
    
    public static void pushStep(Runnable step) {
        navigationStack.push(step);
    }
    
    public static String getInput(Scanner sc, String prompt) {
        System.out.print(prompt);
        String input = sc.nextLine();
        
        if (input != null && "back".equalsIgnoreCase(input.trim())) {
            goBack();
            return null; // Signal to caller that back was pressed
        }
        
        return input;
    }
    
    public static String getInputWithTrim(Scanner sc, String prompt) {
        String input = getInput(sc, prompt);
        return input != null ? input.trim() : null;
    }
    
    private static void goBack() {
        if (navigationStack.size() > 1) {
            navigationStack.pop(); // Remove current step
            Runnable previousStep = navigationStack.peek();
            previousStep.run(); // Go to previous step
        } else if (navigationStack.size() == 1) {
            // If only one step, go back to dashboard
            navigationStack.clear();
            System.out.println("Going back to dashboard...");
            // This will be handled by the calling method
        }
    }
    
    public static boolean isFirstStep() {
        return navigationStack.size() <= 1;
    }
    
    public static void clearStack() {
        navigationStack.clear();
    }
}