package organizer;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import util.Databaseconnection;
import util.SafeInput;

public class CreateTeam {
    Scanner sc = new Scanner(System.in);

    String team_name;
    int coach_id;
    int tournament_id;
    String status;
    String path;
    File logoFile;
    int championships;

    public void connection() {

        try (Connection con = Databaseconnection.getConnection()) {
            String sql = "INSERT INTO teams (team_name, coach_id, tournament_id, team_status, team_logo, total_championships) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, team_name);
            ps.setInt(2, coach_id);
            ps.setInt(3, tournament_id);
            ps.setString(4, status);
            FileInputStream fis = new FileInputStream(logoFile);
            ps.setBinaryStream(5, fis, (int) logoFile.length());
            ps.setInt(6, championships);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Team created successfully!");
            }

            fis.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(" Error while creating team: " + e.getMessage());
        }
    }

    public void getTeamName() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter Team Name: ");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_____________________________________");

                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("Team name cannot be empty.");
                        attempt++;
                    }
                    if (input.matches("^[A-Za-z0-9\\s]{3,}$")) {
                        team_name = input;
                        getCoachId();
                        return;
                    } else {
                        System.out.println("Invalid name! Use letters, numbers, and spaces (min 3 chars).");

                    }
                } catch (Exception e) {
                    System.out.println("Error Occurs : " + e.getMessage());
                }

            }
        }
    }

    public void showAvailableCoachIds() {
        try (Connection con = Databaseconnection.getConnection()) {
            String query = "SELECT c.coach_id, u.user_name FROM coaches c JOIN users u ON c.user_id = u.user_id ";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            System.out.println("Members in Coaches table");
            System.out.println("+-------------------------+");
            System.out.printf("|%-8s| %-15s| \n",
                    "Coach_id", "Coach_name");
            System.out.println("+-------------------------+");
            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                int Coach_ID = rs.getInt("coach_id");
                String User_Name = rs.getString("user_name");
                System.out.printf("|%-8d| %-15s| \n",
                        Coach_ID, User_Name);
                System.out.println("+-------------------------+");
            }
            if (!hasResult) {
                System.out.println("None Coaches are available yet.");
            }

        } catch (Exception e) {
            System.out.println("Error fetching coach list: " + e.getMessage());
        }
    }

    public void getCoachId() {
        int attempt = 0;
        while (attempt < 3) {
            try {
                System.out.println("Select a Coach by ID from the list below:");
                showAvailableCoachIds();
                System.out.println("_______________________________________________");
                System.out.print("Enter Coach ID: ");
                String input = SafeInput.getLine(sc).trim();
                System.out.println("_____________________________________");

                if (input.equalsIgnoreCase("Exit"))
                    return;

                if (input.matches("\\d+")) {
                    coach_id = Integer.parseInt(input);

                    getTournamentId();
                    return;
                } else {
                    System.out.println("Invalid Coach ID! Please enter a numeric value.");
                    attempt++;
                }
            } catch (Exception e) {
                System.out.println("Error Occurred: " + e.getMessage());
                attempt++;
            }
        }
    }

    public void showAvailableTournaments() {
        try (Connection con = Databaseconnection.getConnection()) {
            String query = "SELECT tournament_id, name FROM tournaments ";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            System.out.println("Available Tournaments:");
            System.out.println("+------------------------------+");
            System.out.printf("|%-13s| %-15s| \n",
                    "Tournament_id", "Tournament_name");
            System.out.println("+------------------------------+");
            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                int tournament_ID = rs.getInt("tournament_id");
                String tournament_Name = rs.getString("name");
                System.out.printf("|%-13d| %-15s| \n",
                        tournament_ID, tournament_Name);
                System.out.println("+------------------------------+");
            }
            if (!hasResult) {
                System.out.println("None tournaments are available yet.");
            }

        } catch (Exception e) {
            System.out.println("Error fetching tournament list: " + e.getMessage());
        }
    }

    public void getTournamentId() {
        int attempt = 0;
        while (attempt < 3) {
            try {
                System.out.println("Select a tournament by ID from the list below:");
                showAvailableTournaments();
                System.out.println("_______________________________________________");

                System.out.print("Enter Tournament ID: ");
                String input =SafeInput.getLine(sc).trim();

                System.out.println("_____________________________________");

                if (input.equalsIgnoreCase("Exit"))
                    return;

                if (input.matches("\\d+")) {
                    tournament_id = Integer.parseInt(input);
                    getTeamStatus();
                    return;
                } else {
                    System.out.println("Invalid Tournament ID! Please enter a numeric value.");
                    attempt++;
                }
            } catch (Exception e) {
                System.out.println("Error Occurred: " + e.getMessage());
                attempt++;
            }
        }
    }

    public void getTeamStatus() {
        int attempt = 0;
        while (attempt < 3) {
            System.out.print("Enter Team Status (Active/Inactive/Disqualified): ");
            String input = SafeInput.getLine(sc).trim();
            System.out.println("_____________________________________");

            if (input.equalsIgnoreCase("Exit"))
                return;

            if (input.matches("(?i)Active|Inactive|Disqualified")) {
                status = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
                getTeamLogoPath();
                return;
            } else {
                System.out.println("Invalid status! Choose from Active, Inactive, or Disqualified.");
                attempt++;
            }
        }
    }

    public void getTeamLogoPath() {
        int attempt = 0;
        while (attempt < 3) {
            System.out.print("Enter full path to team logo image: ");
            String input = SafeInput.getLine(sc).trim();
            System.out.println("_____________________________________");

            if (input.equalsIgnoreCase("Exit")) {
                return;
            }
            File file = new File(input);
            if (file.exists() && !file.isDirectory() && file.getName().matches(".*\\.(jpg|jpeg|png)$")) {
                logoFile = file;
                getChampionshipsCount();
                return;
            } else {
                System.out.println("Invalid file path. Please enter a valid image file.");
                attempt++;
            }
        }
    }

    public void getChampionshipsCount() {
        int attempt = 0;
        while (attempt < 3) {
            try {
                System.out.print("Enter total championships won: ");
                String input = SafeInput.getLine(sc).trim();
                System.out.println("_____________________________________");

                if (input.equalsIgnoreCase("Exit"))
                    return;

                if (input.matches("\\d+")) {
                    championships = Integer.parseInt(input);
                    connection();
                    return;
                } else {
                    System.out.println("Invalid input! Please enter a non-negative number.");
                    attempt++;
                }
            } catch (Exception e) {
                System.out.println("Error Occurred: " + e.getMessage());
                attempt++;
            }
        }
    }
}