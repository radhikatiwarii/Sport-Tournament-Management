package coach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import util.Databaseconnection;

import util.InputUtil;

public class PracticeSessionManager {
    static int team_id;
    static int coach_id;
    static LocalDate session_date;
    static LocalTime start_date;
    static LocalTime end_date;
    static String location;
    static String description;
    static int session_id;
    static int Player_id;
    static String status;

    Scanner sc = new Scanner(System.in);

    public void createPracticeSession() {
        try {

            Connection con = Databaseconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO practice_session (team_id, coach_id, session_date, start_time, end_time, location, description) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, team_id);
            ps.setInt(2, coach_id);
            ps.setDate(3, java.sql.Date.valueOf(session_date));
            ps.setTime(4, java.sql.Time.valueOf(start_date));
            ps.setTime(5, java.sql.Time.valueOf(end_date));
            ps.setString(6, location);
            ps.setString(7, description);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Practice session created successfully!" : "Failed to create session.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void markAttendance() {
        try (Connection con = Databaseconnection.getConnection()) {
            PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM session_attendance WHERE session_id = ? AND player_id = ?");
            check.setInt(1, session_id);
            check.setInt(2, Player_id);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                System.out.println(" Attendance already marked for this player in this session.");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO session_attendance (session_id, player_id, status) VALUES (?, ?, ?)");
            ps.setInt(1, session_id);
            ps.setInt(2, Player_id);
            ps.setString(3, status);
            ps.executeUpdate();

            System.out.println(" Attendance marked successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSessionDate() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter session date (yyyy-mm-dd): ");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("Session date cannot be empty, Please enter a valid date:");
                        attempt++;

                    }
                    if (input.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
                        session_date = LocalDate.parse(input);
                        getStartTime();
                        return;
                    } else {
                        System.out.println("Invalid date ! please follow This format (yyyy-mm-dd):");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }
            }
            retryLogic(attempt);
        }
    }

    public void getStartTime() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter start time (HH:mm): ");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit"))
                        return;

                    if (input.isEmpty()) {
                        System.out.println("Start time cannot be empty. Please enter a valid time:");
                        attempt++;
                        continue;
                    }

                    if (input.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
                        start_date = LocalTime.parse(input);
                        getEndTime();
                        return;
                    } else {
                        System.out.println("Invalid time! Please follow this format (HH:mm):");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }
            retryLogic(attempt);
        }
    }

    public void getEndTime() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter end time (HH:mm): ");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit"))
                        return;

                    if (input.isEmpty()) {
                        System.out.println("End time cannot be empty. Please enter a valid time:");
                        attempt++;
                        continue;
                    }

                    if (input.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
                        if (LocalTime.parse(input).isBefore(start_date)) {
                            System.out.println("End time cannot be before start time.");
                            attempt++;
                            continue;
                        }

                        end_date = LocalTime.parse(input);
                        getLocation();
                        return;
                    } else {
                        System.out.println("Invalid time! Please follow this format (HH:mm):");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }
            retryLogic(attempt);
        }
    }

    public void getLocation() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter session location: ");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit"))
                        return;

                    if (input.isEmpty()) {
                        System.out.println("Location cannot be empty. Please enter a valid location:");
                        attempt++;
                        continue;
                    }

                    if (input.length() <= 100) {
                        location = input;
                        getDescription();
                        return;
                    } else {
                        System.out.println("Location too long! Max 100 characters allowed.");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
            }
            retryLogic(attempt);
        }
    }

    public void getDescription() {
        System.out.print("Enter session description (optional, max 255 chars): ");
        String input = sc.nextLine().trim();
        System.out.println("_____________________________________");

        if (!input.isEmpty() && input.length() > 255) {
            System.out.println("Description too long! Trimming to 255 characters.");
            input = input.substring(0, 255);
        }
        description = input;
        createPracticeSession();
        System.out.println(" Session details successfully captured!");
    }

    private void retryLogic(int attempt) {
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
}