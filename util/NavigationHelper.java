package util;

import java.util.Stack;

public class NavigationHelper {
    private static Stack<Runnable> navigationStack = new Stack<>();
    
    public static void pushPage(Runnable page) {
        navigationStack.push(page);
    }
    
    public static void goBack() {
        if (!navigationStack.isEmpty()) {
            navigationStack.pop(); // Remove current page
            if (!navigationStack.isEmpty()) {
                Runnable previousPage = navigationStack.peek();
                previousPage.run(); // Go to previous page
            }
        }
    }
    
    public static boolean checkBackCommand(String input) {
        return "back".equalsIgnoreCase(input.trim());
    }
    
    public static void clearStack() {
        navigationStack.clear();
    }
}