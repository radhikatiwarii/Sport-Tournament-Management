package util;
 
import java.util.Scanner;

public class Password {

        public String getPassword(Scanner sc) {
        UniversalInput.pushStep(() -> getPassword(sc));
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter Your Password");
                    String input =sc.nextLine();
                    if (input == null) return "BACK_COMMAND";  
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return null;
                    }
                     
                    if (input.isEmpty()) {
                        System.out.println("Password cannot be empty, Please enter a valid password:");
                        attempt++;
                        continue;
                    }
                    if (input.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,}$")) {
                       String password =input;
                       return password;
                    } else {
                        System.out.println("____________________________________________________________________");
                        System.out.println("Invalid Password! Please enter a valid password!");
                        System.out.println("Make sure it contains:");
                        System.out.println("- At least 8 characters long.");
                        System.out.println("- Contains both uppercase and lowercase letters.");
                        System.out.println("- Includes at least one digit.");
                        System.out.println("- Includes at least one special character (like @, #, $, etc.).");
                        System.out.println("Please try again.\n");
                        System.out.println("Example: Password123!");
                        System.out.println("_____________________________________________________________________");
                    }

                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occurred: " + e.getMessage());
                }
            }
            retryLogic(attempt, sc);
        }
    }
    private void retryLogic(int attempt, Scanner sc) {
        System.out.println("You have tried 3 Times,");
        int reAttempt = 0;
        while (reAttempt < 3) {
            try {
                System.out.println("-----------------------------");
                System.out.println("[1]. Try Again");
                System.out.println("[2]. Exit");
                System.out.println("-----------------------------");
                System.out.println("Choose an option:");
                
                int choice = InputUtil.chooseInt(sc);
                sc.nextLine();
                
                switch (choice) {
                    case 1:
                        return; // Go back to password input
                    case 2:
                        System.out.println("Exiting....");
                        System.exit(0); // Proper exit
                        return;
                    default:
                        System.out.println("Invalid Choice, Try again:");
                        reAttempt++;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Exception Occurs: " + e.getMessage());
                reAttempt++;
            }
        }
        System.out.println("Invalid attempts! Exiting...");
        System.exit(0);
    }

    
}
