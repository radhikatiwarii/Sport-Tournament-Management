package util;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;

public class Password {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "1234567890123456"; // 16-char secret key (change as needed)
    private Scanner sc = new Scanner(System.in);

    

    // AES Encrypt
    public static String encrypt(String data, String secretKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // AES Decrypt (agar future me use karna ho)
    public static String decrypt(String encryptedData, String secretKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    public String getPassword() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your password :");
                    String input = sc.nextLine().trim();
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
                        // Encrypt password here before storing
                       String password =encrypt(input, SECRET_KEY);

                        System.out.println("Your password has been encrypted and stored securely.");
                        // If you want to verify, you can print decrypted:
                        // System.out.println("Decrypted password: " + decrypt(password, SECRET_KEY));
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
            retryLogic(attempt);
        }
    }
private void retryLogic(int attempt)

    {
        System.out.println("You have tried 3 Times,");
        int reAttempt = 0;
        while (reAttempt < 3) {
            try {
                System.out.println("-----------------------------");
                System.out.println("[1]. Try Again");
                System.out.println("[2]. exit");
                System.out.println("-----------------------------");

                System.out.println("choose an option :");
                int choice = InputUtil.chooseInt(sc);
                sc.nextLine();
                switch (choice) {
                    case 1: {
                        return;
                    }
                    case 2: {
                        System.out.println("Exiting....");
                        break;
                    }
                    default: {
                        System.out.println("Invalid Choice , Try again:");
                        reAttempt++;
                        break;
                    }
                }
                if (reAttempt == 3) {
                    System.out.println("Invalid attemts ! Exitting...");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Exception Occurs : " + e.getMessage());
                reAttempt++;
            }
        }
    }

    
}
