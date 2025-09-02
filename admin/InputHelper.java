package admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import util.NavigationHelper;
import util.NavigationHelper;

public class InputHelper {
     static Scanner scanner = new Scanner(System.in);

  
    public static String getString(String prompt, int[] attempt) {
        while (attempt[0] < 3) {
            System.out.print(prompt);
            try {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    if (input == null) return null;
                    input = input.trim();
                    // Check for back command
                    if (NavigationHelper.checkBackCommand(input)) {
                        NavigationHelper.goBack();
                        return null;
                    }
                    return input;
                } else {
                    System.out.println("\nInput was interrupted.. Please provide valid input.");
                    System.out.println("------------------------------------------------------------\n");
                    attempt[0]++;
                    scanner = new Scanner(System.in);
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                System.out.println("------------------------------------------------------------\n");
                attempt[0]++;
            }
        }
        if (attempt[0] >= 3) {
            System.out.println("Lost your three attempts");
            System.out.println("Returning to login page");
        }
        return null;
    }

    public static String getInput() {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
        return reader.readLine(); // Reads the entire input line
    } catch (Exception e) {
        System.out.println("Error reading input: " + e.getMessage());
        return "";
    }
}
}