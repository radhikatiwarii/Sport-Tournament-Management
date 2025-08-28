package admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InputHelper {
     static Scanner scanner = new Scanner(System.in);

  
    public static String getString(String prompt, int[] attempt) {
        while (attempt[0] < 3) {
            System.out.print(prompt);
            try {
                

                if (scanner.hasNextLine()) {
                    return scanner.nextLine().trim();
                } else {
                    System.out.println("\nInput was interrupted.. Please provide valid input.");
                    System.out.println("------------------------------------------------------------\n");

                    attempt[0]++;
                    scanner = new Scanner(System.in);
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                System.out.println("------------------------------------------------------------\n");

            }
        }
        if (attempt[0]==3) {
            System.out.println("losse your three attempts");
            System.out.println("returning to login page");
            
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