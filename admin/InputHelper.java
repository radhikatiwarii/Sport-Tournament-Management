package admin;

import util.UniversalInput;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InputHelper {
    static Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt, int[] attempt) {
        while (attempt[0] < 3) {
            try {
                String input = UniversalInput.getInputTrim(scanner, prompt);
                if (input == null) return null; // Back was pressed
                return input;
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
            return reader.readLine();
        } catch (Exception e) {
            System.out.println("Error reading input: " + e.getMessage());
            return "";
        }
    }
}