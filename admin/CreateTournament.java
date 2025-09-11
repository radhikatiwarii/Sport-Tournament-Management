package admin;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.sql.Date;
import util.Databaseconnection;
import util.InputUtil;
import util.SafeInput;

public class CreateTournament {
    static String status;
    static String name;
    static LocalDate start_date;
    static LocalDate end_date;
    static int max_teams;
    static LocalDate registration_opening_date;
    static LocalDate registration_closing_date;
    Scanner sc = new Scanner(System.in);

    public static void createTournament() {
        try (Connection con = Databaseconnection.getConnection()) {

            String query = "INSERT INTO tournaments (name, start_date, end_date,status, Max_allowed, registration_opening_date, registration_closing_date) "
                    +
                    "VALUES (?, ?, ?, ?,?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setDate(2, Date.valueOf(start_date));
            ps.setDate(3, Date.valueOf(end_date));
            ps.setString(4, status);
            ps.setInt(5, max_teams);
            ps.setDate(6, Date.valueOf(registration_opening_date));
            ps.setDate(7, Date.valueOf(registration_closing_date));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Tournament created successfully!");
            } else {
                System.out.println(" Failed to create tournament.");
            }
        } catch (Exception e) {
            System.out.println("Error while creating tournament:");
            e.printStackTrace();
        }
    }

    void getTournamentName() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter Tournament Name :");
                    String input = SafeInput.getLine(sc).trim();
                    if (input == null)
                        return; // Back pressed
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("tournament name cannot be empty, Please enter a valid name:");
                        attempt++;

                    }

                    if (input.matches("^[A-Z][a-zA-Z\\s]{2,}$")) {
                        name = input;
                        getStartDate();
                        return;
                    } else {
                        System.out.println("Invalid Tournament Name ! please enter a valid name !");
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

    void getStartDate() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter Tournament Start Date :");
                    String input = SafeInput.getLine(sc).trim();
                    if (input == null)
                        return; // Back pressed
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("Tournament Starting Date cannot be empty, Please enter a valid date:");
                        attempt++;

                    }

                    if (input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                        start_date = LocalDate.parse(input);
                        getEndDate();
                        return;
                    } else {
                        System.out.println("Invalid Tournament Date ! please Follow this format yyyy-mm-dd !");
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

    void getEndDate() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter Tournament End Date :");
                    String input = SafeInput.getLine(sc).trim();
                    if (input == null)
                        return; // Back pressed
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("Tournament Ending Date cannot be empty, Please enter a valid date:");
                        attempt++;

                    }

                    if (input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                        LocalDate inputDate = LocalDate.parse(input);
                        if (inputDate.isAfter(start_date)) {
                            end_date = LocalDate.parse(input);
                            getStatus();
                            return;
                        } else {
                            System.out.println("End date must be after start date (" + start_date + ").");
                            System.out.println("________________________________________________________________");
                        }
                    }

                    else {
                        System.out.println("Invalid Tournament Date ! please Follow this format yyyy-mm-dd !");
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

    public void getStatus() {

        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Status");
                    String input = SafeInput.getLine(sc).trim();
                    if (input == null)
                        return; // Back pressed
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("username cannot be empty, Please enter a valid name:");
                        attempt++;

                    }

                    if (input.matches("(?i)open|closed")) {
                        status=input;
                        getMaxAllowed();
                        return;
                    }

                    else {
                        System.out.println("Invalid Status ! please set only open or closed !");
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

    public void getMaxAllowed() {

        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Max Teams Allowed :");
                    String input = SafeInput.getLine(sc).trim();
                    if (input == null)
                        return; // Back pressed
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("This field can't be empty, Please enter a valid number:");
                        attempt++;

                    }

                    if (input.matches("^\\d{1,2}$")) {
                        max_teams=Integer.parseInt(input);
                        getRegOpeningDate();
                        return;
                    }

                    else {
                        System.out.println("Invalid Input ! please enter only valid number!");
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

    void getRegOpeningDate() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Registration Opening Date :");
                    String input = SafeInput.getLine(sc).trim();
                    if (input == null)
                        return; // Back pressed
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("Registration Opening Date cannot be empty, Please enter a valid date:");
                        attempt++;

                    }

                    if (input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                        LocalDate inputDate = LocalDate.parse(input);
                        if (inputDate.isBefore(start_date)) {
                            registration_opening_date = LocalDate.parse(input);
                            getRegClosingDate();
                            return;
                        } else {
                            System.out.println("Registration Opening Date  must be before date of start date ("
                                    + start_date + ").");
                            System.out.println("________________________________________________________________");
                        }
                    } else {
                        System.out.println("Invalid  Date ! please Follow this format yyyy-mm-dd !");
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

    void getRegClosingDate() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Registration Closing Date :");
                    String input = SafeInput.getLine(sc).trim();
                    if (input == null)
                        return; // Back pressed
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("Registration Closing Date cannot be empty, Please enter a valid date:");
                        attempt++;

                    }

                    if (input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                        LocalDate inputDate = LocalDate.parse(input);
                        if (inputDate.isAfter(registration_opening_date) && inputDate.isBefore(start_date)) {
                            registration_closing_date = LocalDate.parse(input);
                            createTournament();
                            return;
                        } else {
                            System.out.println("Registration Closing Date  must be before date of start date ("
                                    + start_date + "), and after the Registration Opening Date.");
                            System.out.println("________________________________________________________________");
                        }
                    } else {
                        System.out.println("Invalid  Date ! please Follow this format yyyy-mm-dd !");
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
                        return;
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