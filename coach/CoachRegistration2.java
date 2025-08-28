package coach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import util.Databaseconnection;
import util.InputUtil;

public class CoachRegistration2 {
    static int user_id;
    static int fees;
    static String specialization;
    static String description;
    static int years_of_experience;
    static int coach_id;

    Scanner sc = new Scanner(System.in);
    public CoachRegistration2(int userId) {
    user_id = userId;
}



    private void retryLogic(int attempt)

    {
        System.out.println("You have tried 3 Times,");
        int reAttempt = 0;
        while (reAttempt < 3) {
            try {
                System.out.println("[1]. Try Again");
                System.out.println("[2]. exit");
                System.out.println("choose an option :");
                int choice = InputUtil.chooseInt(sc);
                sc.nextLine();
                switch (choice) {
                    case 1: {
                        return;
                    }
                    case 2: {
                        System.out.println("Exiting....");
                        return;
                    }
                    default: {
                        System.out.println("Invalid Choice , Try again:");
                        reAttempt++;
                        break;
                    }
                }
                if (reAttempt == 3) {
                    System.out.println("Invalid attempts ! Exitting...");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Exception Occurs : " + e.getMessage());
                reAttempt++;
            }
        }
    }

    void CoachInformation() {
        getCoachFees();
    }

    public void getCoachFees() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("please Enter your fees.");
                    String input = sc.nextLine();

                    System.out.println("_____________________________________");
                    if (input.equals("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("fees cannot be empty, Please enter a valid fees:");
                        attempt++;

                    }

                    if (input.matches("\\d+")) {
                        fees = Integer.parseInt(input);
                        getSpecialization();
                        return;
                    }

                    else {
                        System.out.println("Invalid fees ! please enter only digits!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void getSpecialization() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Please enter the field in which you can teach well. ");
                    String input = sc.nextLine();

                    System.out.println("_____________________________________");
                    if (input.equals("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("this field  cannot be empty, Please enter a valid information:");
                        attempt++;

                    }

                    if (input.matches("[a-zA-Z ]+")) {
                        specialization = input;
                        getDescription();
                        return;
                    }

                    else {
                        System.out.println("Invalid field ! please enter only letters!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void getDescription() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Please tell us about you Something .");
                    String input = sc.nextLine();

                    System.out.println("_____________________________________");
                    if (input.equals("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("this field cannot be empty, Please enter a valid description:");
                        attempt++;

                    }

                    if (input.matches("[a-zA-Z ,.']+")) {
                        description = input;
                        getExperience();
                        return;
                    }

                    else {
                        System.out.println("Invalid field! please enter only letters!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void getExperience() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("enter yor experience of coaching .");
                    String input = sc.nextLine().trim();

                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("Experience cannot be empty, Please enter a valid number:");
                        attempt++;
                        continue;

                    }

                    if (input.matches("\\d+")) {
                        years_of_experience = Integer.parseInt(input);
                        getCoachConnection();
                        return;
                    }

                    else {
                        System.out.println("Invalid Experience ! please enter only digits!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    void getCoachConnection() {

        try (Connection con = Databaseconnection.getConnection()) {
            String query = "insert into coaches(user_id,fees,specialization,description,years_of_experience)values(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, user_id);
            ps.setInt(2, fees);
            ps.setString(3, specialization);
            ps.setString(4, description);
            ps.setInt(5, years_of_experience);

            int choose = ps.executeUpdate();

            if (choose > 0) {

                System.out.println("Registration Successful for coach ");
                CoachDashboard cd = new CoachDashboard();
                cd.showDashboard(sc);
            } else {
                System.out.println("Registration failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
