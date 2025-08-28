package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import util.Databaseconnection;
import util.InputUtil;

public class VenueManagement {
    static String available_dates;
    static String name;
    static String location;
    static int capacity;

    Scanner sc = new Scanner(System.in);

    void display() {
        while (true) {

            System.out.println("Manage Your Venue !");
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");
            System.out.println("1. View Available Venue");
            System.out.println("2. Add new Venue");
            System.out.println("3. Update Venue Status");
            System.out.println("4. Back");
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");

            int choose = InputUtil.chooseInt(sc);
            sc.nextLine();
            switch (choose) {
                case 1: {
                    viewVenue();
                    break;
                }
                case 2: {
                    getVenueName();
                    break;
                }

                case 3: {

                    break;
                }
                case 4: {
                    System.out.println("Back ");
                    return;
                }

                default:
                    break;
            }
        }
    }

    public void getVenueName() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your Venue name :");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("venue Name cannot be empty, Please enter a valid name:");
                        attempt++;
                    } else if (input.matches("^[A-Za-z][A-Za-z\\s'-]{2,49}$")) {
                        name = input;
                        getlocation();
                        return;
                    } else {
                        System.out.println("Invalid venue ! please match your value with regex!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void getlocation() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your  venue location :");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("venue location cannot be empty, Please enter a valid location:");
                        attempt++;
                    } else if (input.matches("^[A-Za-z0-9\s,'-]{3,100}$")) {
                        location = input;
                        getCapacity();
                        return;
                    } else {
                        System.out.println("Invalid location ! please match your value with regex!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void getCapacity() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your  venue Capacity :");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("Capacity cannot be empty, Please enter a valid number:");
                        attempt++;
                    } else if (input.matches("^[1-9][0-9]{0,3}$")) {
                        capacity = Integer.parseInt(input);
                        insertVenue();
                        return;
                    } else {
                        System.out.println("Invalid Capacity ! please match your value with regex!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void insertVenue() {
        try (Connection conn = Databaseconnection.getConnection()) {
            String query = "INSERT INTO Venue (name, location, capacity) VALUES (?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, capacity);

            stmt.executeUpdate();
            System.out.println("Venue inserted successfully!");
            while (true) {
                System.out.println("Do you want to add another venue? (yes/no): ");
                String response = sc.nextLine().trim().toLowerCase();

                if (response.equals("yes")) {
                    insertVenue();
                    break;
                } else if (response.equals("no")) {
                    System.out.println("Returning to main menu...");
                    break;
                } else {
                    System.out.println("Invalid input. Please type 'yes' or 'no'.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewVenue() {
        try (Connection con = Databaseconnection.getConnection()) {
            Statement stmt = con.createStatement();
            String query = "Select * from venue ";
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("Viewing All Venue !");
            System.out.println("+---------------------------------------------------------------+");
            System.out.printf("| %-8s| %-20s| %-20s| %-8s|  \n",
                    "Venue_id", "Name", "Location", "Capacity");
            System.out.println("+---------------------------------------------------------------+");
            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                int venue_id = rs.getInt("venue_id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");

                System.out.printf("| %-8d| %-20s| %-20s| %-8s|  \n",
                        venue_id, name, location, capacity);
                System.out.println("+---------------------------------------------------------------+");
            }
            if (!hasResult) {
                System.out.println("Non venue available yet.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retryLogic(int attempt) {
        System.out.println("You have tried 3 Times,");
        int reAttempt = 0;
        while (reAttempt < 3) {
            try {
                System.out.println("[1]. Try Again");
                System.out.println("[2]. exit");
                System.out.println("choose an option :");
                int choice = sc.nextInt();
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
