package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import util.Databaseconnection;
 import util.SafeInput;

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
            System.out.println("4. Assign Venue to Tournament");
            System.out.println("5. Back");
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");

            int choose = SafeInput.getInt(sc);
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
                    updateVenueStatus();
                    break;
                }
                case 4: {
                    assignVenueToTournament();
                    break;
                }
                case 5: {
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
                    String input = SafeInput.getLine(sc);
                    
                    if (input == null) {
                        System.out.println("Input terminated. Exiting...");
                        return;
                    }
                    
                    input = input.trim();
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
                    String input = SafeInput.getLine(sc);
                    
                    if (input == null) {
                        System.out.println("Input terminated. Exiting...");
                        return;
                    }
                    
                    input = input.trim();
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
                    String input = SafeInput.getLine(sc);
                    
                    if (input == null) {
                        System.out.println("Input terminated. Exiting...");
                        return;
                    }
                    
                    input = input.trim();
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
            System.out.println("+-------------------------------------------------------------------------+");
            System.out.printf("| %-8s| %-30s| %-20s| %-8s|  \n",
                    "Venue_id", "Name", "Location", "Capacity");
            System.out.println("+-------------------------------------------------------------------------+");
            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                int venue_id = rs.getInt("venue_id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");

                System.out.printf("| %-8d| %-30s| %-20s| %-8s|  \n",
                        venue_id, name, location, capacity);
                System.out.println("+-------------------------------------------------------------------------+");
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
                int choice =SafeInput.getInt(sc);
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
    
    public void updateVenueStatus() {
        viewVenue(); // Show available venues first
        
        System.out.print("Enter Venue ID to update (or 'back' to return): ");
        String venueInput = SafeInput.getLine(sc);
        
        if (venueInput == null) {
            return; // Back was pressed
        }
        
        venueInput = venueInput.trim();
        if (venueInput.equalsIgnoreCase("back")) {
            return;
        }
        
        int venueId;
        try {
            venueId = Integer.parseInt(venueInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid venue ID! Please enter a number.");
            return;
        }
        
        try (Connection con = Databaseconnection.getConnection()) {
            // Check if venue exists and get current details
            String checkQuery = "SELECT name, location, capacity FROM venue WHERE venue_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkQuery);
            checkPs.setInt(1, venueId);
            ResultSet rs = checkPs.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Venue ID not found!");
                return;
            }
            
            String currentName = rs.getString("name");
            String currentLocation = rs.getString("location");
            int currentCapacity = rs.getInt("capacity");
            
            System.out.println("Current Details:");
            System.out.println("Name: " + currentName);
            System.out.println("Location: " + currentLocation);
            System.out.println("Capacity: " + currentCapacity);
            System.out.println();
            
            System.out.println("What do you want to update?");
            System.out.println("1. Name");
            System.out.println("2. Location");
            System.out.println("3. Capacity");
            System.out.print("Choose option (1-3) or 'back' to return: ");
            
            String choiceInput = SafeInput.getLine(sc);
            
            if (choiceInput == null) {
                return; // Back was pressed
            }
            
            choiceInput = choiceInput.trim();
            if (choiceInput.equalsIgnoreCase("back")) {
                return;
            }
            
            int choice;
            try {
                choice = Integer.parseInt(choiceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                return;
            }
            
            switch (choice) {
                case 1:
                    updateVenueName(con, venueId);
                    break;
                case 2:
                    updateVenueLocation(con, venueId);
                    break;
                case 3:
                    updateVenueCapacity(con, venueId);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            
        } catch (SQLException e) {
            System.out.println("Error updating venue: " + e.getMessage());
        }
    }
    
    private void updateVenueName(Connection con, int venueId) throws SQLException {
        int attempt = 0;
        while (attempt < 3) {
            try {
                System.out.print("Enter new venue name (or 'back' to return): ");
                String input = SafeInput.getLine(sc);
                
                if (input == null) {
                    return; // Back was pressed
                }
                
                input = input.trim();
                if (input.equalsIgnoreCase("back")) {
                    return;
                }
                
                if (input.isEmpty()) {
                    System.out.println("Venue name cannot be empty!");
                    attempt++;
                    continue;
                }
                
                if (input.matches("^[A-Za-z][A-Za-z\\s'-]{2,49}$")) {
                    String query = "UPDATE venue SET name = ? WHERE venue_id = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, input);
                    ps.setInt(2, venueId);
                    
                    if (ps.executeUpdate() > 0) {
                        System.out.println("Venue name updated successfully!");
                        return;
                    }
                } else {
                    System.out.println("Invalid name format! Use letters, spaces, apostrophes, hyphens (3-50 chars)");
                    attempt++;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                attempt++;
            }
        }
        System.out.println("Too many invalid attempts!");
    }
    
    private void updateVenueLocation(Connection con, int venueId) throws SQLException {
        int attempt = 0;
        while (attempt < 3) {
            try {
                System.out.print("Enter new venue location (or 'back' to return): ");
                String input = SafeInput.getLine(sc);
                
                if (input == null) {
                    return; // Back was pressed
                }
                
                input = input.trim();
                if (input.equalsIgnoreCase("back")) {
                    return;
                }
                
                if (input.isEmpty()) {
                    System.out.println("Venue location cannot be empty!");
                    attempt++;
                    continue;
                }
                
                if (input.matches("^[A-Za-z0-9\\s,'-]{3,100}$")) {
                    String query = "UPDATE venue SET location = ? WHERE venue_id = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, input);
                    ps.setInt(2, venueId);
                    
                    if (ps.executeUpdate() > 0) {
                        System.out.println("Venue location updated successfully!");
                        return;
                    }
                } else {
                    System.out.println("Invalid location format! Use letters, numbers, spaces, commas, apostrophes, hyphens (3-100 chars)");
                    attempt++;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                attempt++;
            }
        }
        System.out.println("Too many invalid attempts!");
    }
    
    private void updateVenueCapacity(Connection con, int venueId) throws SQLException {
        int attempt = 0;
        while (attempt < 3) {
            try {
                System.out.print("Enter new venue capacity (1-9999) (or 'back' to return): ");
                String input = SafeInput.getLine(sc);
                
                if (input == null) {
                    return; // Back was pressed
                }
                
                input = input.trim();
                if (input.equalsIgnoreCase("back")) {
                    return;
                }
                
                if (input.isEmpty()) {
                    System.out.println("Venue capacity cannot be empty!");
                    attempt++;
                    continue;
                }
                
                if (input.matches("^[1-9][0-9]{0,3}$")) {
                    int newCapacity = Integer.parseInt(input);
                    String query = "UPDATE venue SET capacity = ? WHERE venue_id = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, newCapacity);
                    ps.setInt(2, venueId);
                    
                    if (ps.executeUpdate() > 0) {
                        System.out.println("Venue capacity updated successfully!");
                        return;
                    }
                } else {
                    System.out.println("Invalid capacity format! Enter a number between 1-9999");
                    attempt++;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                attempt++;
            }
        }
        System.out.println("Too many invalid attempts!");
    }
    
    public void assignVenueToTournament() {
        int attempt = 0;
        while (attempt < 3) {
            try {
                System.out.println("\n=== ASSIGN VENUE TO TOURNAMENT ===");
                
                // Show available venues
                viewVenue();
                
                System.out.print("Enter Venue ID to assign (or 'back' to return): ");
                String venueInput = SafeInput.getLine(sc);
                
                if (venueInput == null) {
                    System.out.println("Input terminated. Returning to menu...");
                    return;
                }
                
                venueInput = venueInput.trim();
                if (venueInput.equalsIgnoreCase("back")) {
                    return;
                }
                
                if (venueInput.isEmpty()) {
                    System.out.println("Venue ID cannot be empty!");
                    attempt++;
                    continue;
                }
                
                int venueId;
                try {
                    venueId = Integer.parseInt(venueInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid venue ID! Please enter a valid number.");
                    attempt++;
                    continue;
                }
                
                // Show organizer's tournaments
                showOrganizerTournaments();
                
                System.out.print("Enter Tournament ID to assign venue (or 'back' to return): ");
                String tournamentInput = SafeInput.getLine(sc);
                
                if (tournamentInput == null) {
                    System.out.println("Input terminated. Returning to menu...");
                    return;
                }
                
                tournamentInput = tournamentInput.trim();
                if (tournamentInput.equalsIgnoreCase("back")) {
                    return;
                }
                
                if (tournamentInput.isEmpty()) {
                    System.out.println("Tournament ID cannot be empty!");
                    attempt++;
                    continue;
                }
                
                int tournamentId;
                try {
                    tournamentId = Integer.parseInt(tournamentInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid tournament ID! Please enter a valid number.");
                    attempt++;
                    continue;
                }
                
                // Check venue availability for tournament dates
                if (checkVenueAvailability(venueId, tournamentId)) {
                    // Perform the assignment
                    performVenueAssignment(venueId, tournamentId);
                    return;
                } else {
                    System.out.println("Venue is not available for the tournament dates!");
                    attempt++;
                }
                
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                attempt++;
            }
        }
        
        System.out.println("Too many failed attempts. Returning to menu...");
        retryLogic(attempt);
    }
    
    private void showOrganizerTournaments() {
        try (Connection con = Databaseconnection.getConnection()) {
            String query = "SELECT t.tournament_id, t.name, t.start_date, t.end_date, " +
                          "CASE WHEN t.venue_id IS NULL THEN 'No Venue' ELSE v.name END as current_venue " +
                          "FROM tournaments t " +
                          "LEFT JOIN venue v ON t.venue_id = v.venue_id " +
                          "JOIN organizers o ON t.tournament_id = o.tournament_id " +
                          "WHERE o.organizer_id = ?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, util.SessionManager.getOrganizerId());
            ResultSet rs = ps.executeQuery();
            
            System.out.println("\nYour Tournaments:");
            System.out.println("+---------------+-------------------------+------------+------------+--------------------+");
            System.out.printf("| %-13s | %-23s | %-10s | %-10s | %-18s |\n",
                    "Tournament ID", "Tournament Name", "Start Date", "End Date", "Current Venue");
            System.out.println("+---------------+-------------------------+------------+------------+--------------------+");
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.printf("| %-13d | %-23s | %-10s | %-10s | %-18s |\n",
                        rs.getInt("tournament_id"),
                        rs.getString("name"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("current_venue"));
            }
            
            if (!hasResults) {
                System.out.println("| No tournaments assigned to you.                                                     |");
            }
            
            System.out.println("+---------------+-------------------------+------------+------------+--------------------+");
            
        } catch (Exception e) {
            System.out.println("Error fetching tournaments: " + e.getMessage());
        }
    }
    
    private boolean checkVenueAvailability(int venueId, int tournamentId) {
        try (Connection con = Databaseconnection.getConnection()) {
            // Get tournament dates
            String tournamentQuery = "SELECT start_date, end_date, name FROM tournaments WHERE tournament_id = ?";
            PreparedStatement tournamentPs = con.prepareStatement(tournamentQuery);
            tournamentPs.setInt(1, tournamentId);
            ResultSet tournamentRs = tournamentPs.executeQuery();
            
            if (!tournamentRs.next()) {
                System.out.println("Tournament not found!");
                return false;
            }
            
            String startDate = tournamentRs.getString("start_date");
            String endDate = tournamentRs.getString("end_date");
            String tournamentName = tournamentRs.getString("name");
            
            // Check if venue is already booked for overlapping dates
            String conflictQuery = "SELECT t.name, t.start_date, t.end_date " +
                                 "FROM tournaments t " +
                                 "WHERE t.venue_id = ? " +
                                 "AND t.tournament_id != ? " +
                                 "AND ((t.start_date <= ? AND t.end_date >= ?) " +
                                 "OR (t.start_date <= ? AND t.end_date >= ?) " +
                                 "OR (t.start_date >= ? AND t.end_date <= ?))";
            
            PreparedStatement conflictPs = con.prepareStatement(conflictQuery);
            conflictPs.setInt(1, venueId);
            conflictPs.setInt(2, tournamentId);
            conflictPs.setString(3, startDate);
            conflictPs.setString(4, startDate);
            conflictPs.setString(5, endDate);
            conflictPs.setString(6, endDate);
            conflictPs.setString(7, startDate);
            conflictPs.setString(8, endDate);
            
            ResultSet conflictRs = conflictPs.executeQuery();
            
            if (conflictRs.next()) {
                System.out.println(" VENUE CONFLICT DETECTED!");
                System.out.println("Tournament: " + tournamentName + " (" + startDate + " to " + endDate + ")");
                System.out.println("Conflicts with: " + conflictRs.getString("name") + 
                                 " (" + conflictRs.getString("start_date") + " to " + conflictRs.getString("end_date") + ")");
                return false;
            }
            
            System.out.println(" Venue is available for the tournament dates.");
            System.out.println("Tournament: " + tournamentName + " (" + startDate + " to " + endDate + ")");
            return true;
            
        } catch (Exception e) {
            System.out.println("Error checking venue availability: " + e.getMessage());
            return false;
        }
    }
    
    private void performVenueAssignment(int venueId, int tournamentId) {
        try (Connection con = Databaseconnection.getConnection()) {
            // Validate venue exists and get details
            String venueCheck = "SELECT name, location, capacity FROM venue WHERE venue_id = ?";
            PreparedStatement venuePs = con.prepareStatement(venueCheck);
            venuePs.setInt(1, venueId);
            ResultSet venueRs = venuePs.executeQuery();
            
            if (!venueRs.next()) {
                System.out.println(" Venue ID not found!");
                return;
            }
            String venueName = venueRs.getString("name");
            String venueLocation = venueRs.getString("location");
            int venueCapacity = venueRs.getInt("capacity");
            
            // Validate tournament exists and organizer has access
            String tournamentCheck = "SELECT t.name, t.start_date, t.end_date, t.Max_allowed FROM tournaments t " +
                                   "JOIN organizers o ON t.tournament_id = o.tournament_id " +
                                   "WHERE t.tournament_id = ? AND o.organizer_id = ?";
            PreparedStatement tournamentPs = con.prepareStatement(tournamentCheck);
            tournamentPs.setInt(1, tournamentId);
            tournamentPs.setInt(2, util.SessionManager.getOrganizerId());
            ResultSet tournamentRs = tournamentPs.executeQuery();
            
            if (!tournamentRs.next()) {
                System.out.println(" Tournament not found or you don't have access to it!");
                return;
            }
            
            String tournamentName = tournamentRs.getString("name");
            String startDate = tournamentRs.getString("start_date");
            String endDate = tournamentRs.getString("end_date");
            int maxTeams = tournamentRs.getInt("Max_allowed");
            
            // Check venue capacity vs tournament requirements
            int estimatedAttendees = maxTeams * 50; // Rough estimate
            if (venueCapacity < estimatedAttendees) {
                System.out.println(" WARNING: Venue capacity (" + venueCapacity + ") might be insufficient for tournament with " + maxTeams + " teams.");
                System.out.print("Do you want to continue? (yes/no): ");
                
                String confirmation = SafeInput.getLine(sc);
                if (confirmation == null || !confirmation.trim().equalsIgnoreCase("yes")) {
                    System.out.println("Venue assignment cancelled.");
                    return;
                }
            }
            
            // Update tournament with venue
            String updateQuery = "UPDATE tournaments SET venue_id = ? WHERE tournament_id = ?";
            PreparedStatement updatePs = con.prepareStatement(updateQuery);
            updatePs.setInt(1, venueId);
            updatePs.setInt(2, tournamentId);
            
            int rowsUpdated = updatePs.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(" VENUE ASSIGNED SUCCESSFULLY!");
                System.out.println("======================================");
                System.out.println(" Tournament: " + tournamentName);
                System.out.println(" Dates: " + startDate + " to " + endDate);
                System.out.println(" Venue: " + venueName);
                System.out.println(" Location: " + venueLocation);
                System.out.println(" Capacity: " + venueCapacity);
                System.out.println("======================================");
            } else {
                System.out.println(" Failed to assign venue! Please try again.");
            }
            
        } catch (Exception e) {
            System.out.println(" Error assigning venue: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
