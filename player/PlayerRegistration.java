package player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import util.Databaseconnection;
import util.InputUtil;
import util.Validation;
import util.Password;

public class PlayerRegistration {
    static int user_id;
    static String user_name;
    static String phone_no;
    static String email;
    static String password;
    static String role;

    Scanner sc = new Scanner(System.in);
    Validation validator = new Validation();

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

    public void getFullName() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your full name :");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("username cannot be empty, Please enter a valid name:");
                        attempt++;

                    }

                    if (input.matches("^[A-Z][a-z]*\\s[A-Z][a-z]*$")) {
                        user_name = input;
                        getPhoneNo();
                        return;
                    }

                    else {
                        System.out.println("Invalid Name ! please follow This format Firstname Lastname!");
                        System.out.println("________________________________________________________________");

                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                    System.out.println("___________________________________________");

                }

            }
            retryLogic(attempt);
        }
    }

    public void getPhoneNo() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your Mobile no. :");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");

                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("Mobile No. cannot be empty, Please enter a valid number:");
                        attempt++;
                    }

                    if (input.matches("^[6-9]\\d{9}$")) {
                        phone_no = input;
                        getEmail();
                        return;

                    } else {
                        System.out.println("Invalid Number ! please Enter 10 digits & start with 6,7,8,9:");

                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("Error Occurs : " + e.getMessage());
                }
            }
            retryLogic(attempt);
        }
    }

    public void getEmail() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your Email_Id :");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("Email cannot be empty, Please enter a valid Email:");
                        attempt++;
                    }

                    if (input.matches("^[a-z]+[a-z]\\.[a-z]+[0-9]{4}@ssism\\.org$")) {
                        email = input;
                        getPassword();
                        return;
                    }

                    if (input.matches("^[a-z]+[a-z]+\\d+@gmail\\.com$")) {
                        email = input;
                        getPassword();
                        return;
                    }

                    if (input.matches("^[a-z]+[a-z]+\\d+@yahoo\\.com$")) {
                        email = input;
                        getPassword();
                        return;
                    }

                    else {
                        System.out.println("Invalid Email ! please enter email yahoo , gmail or ssism only!");
                    }
                    attempt++;
                }

                catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void getPassword() {
        Password passwordUtil = new Password();
        password = passwordUtil.getPassword();
        if (password != null) {
            System.out.println("Encrypted Password: " + password);
        }
        getRole();
    }

    void getRole() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {

                try {
                    System.out.println("Please specify your role (player, coach, admin, organizer):");
                    String inputRole = sc.nextLine().trim().toLowerCase();
                    System.out.println("_____________________________________");
                    if (inputRole.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (inputRole.isEmpty()) {
                        System.out.println("Role cannot be empty, Please enter your password:");
                        attempt++;
                    }

                    if (inputRole.equals("player")) {

                        role = "player";
                        Connection();
                        return;

                    } else {
                        System.out.println("Invalid role! Since you are registering, only 'player' is allowed.");

                        continue;
                    }

                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }

            retryLogic(attempt);
        }
    }

    void Connection() {
        try (Connection con = Databaseconnection.getConnection()) {
            String query = "insert into users(user_id,user_name,email,password,phone_no,role)values(?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user_id);
            ps.setString(2, user_name);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, phone_no);
            ps.setString(6, role);

            int choose = ps.executeUpdate();

            if (choose > 0) {
                System.out.println("Registration Succesfull !");
                PlayerDashboard pd = new PlayerDashboard();
                pd.showDashboard(sc);
                return;

            } else {
                System.out.println("Registration failed !");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}