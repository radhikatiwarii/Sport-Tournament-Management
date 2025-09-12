package organizer;

import util.Databaseconnection;
 import util.SafeInput;
import util.SessionManager;
import util.OrganizerTournamentValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.SQLException;

public class MatchSchedule {

    Scanner sc = new Scanner(System.in);
    static int tournamentId;
    static String date;

    public void showAvailableTeams() {
        String query = "SELECT team_id, team_name FROM teams";

        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            System.out.println("Available Teams in Teams Table are given Below :");
            System.out.println("+-------------------------+");
            System.out.printf("|%-8s| %-15s| \n",
                    "Team_ID", "Team_Name");
            System.out.println("+-------------------------+");
            boolean hasResult = false;
            while (rs.next()) {
                hasResult = true;
                int Team_ID = rs.getInt("team_id");
                String Team_Name = rs.getString("team_name");
                System.out.printf("|%-8d| %-15s| \n",
                        Team_ID, Team_Name);
                System.out.println("+-------------------------+");
            }
            if (!hasResult) {
                System.out.println("Non Teams are available yet.");
            }

        } catch (Exception e) {
            System.out.println("Error fetching team list.");
        }
    }

    public void scheduleMatch() {
        showAvailableTeams();

        try (Connection con = Databaseconnection.getConnection()) {
            System.out.print("Enter Team 1 ID: ");
            int team1Id = SafeInput.getInt(sc);

            System.out.print("Enter Team 2 ID: ");
            int team2Id = SafeInput.getInt(sc);

            if (team1Id == team2Id) {
                System.out.println(" Team 1 and Team 2 cannot be the same.");
                return;
            }
            if (!isValidTeam(con, team1Id) || !isValidTeam(con, team2Id)) {
                System.out.println(" One or both team IDs are invalid.");
                return;
            }
            sc.nextLine();
            getDate();

            String query = "INSERT INTO matches (team1id, team2id, match_date, tournamentid) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, team1Id);
                ps.setInt(2, team2Id);
                ps.setString(3, date);
                ps.setInt(4, tournamentId);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println(" Match scheduled successfully!");
                } else {
                    System.out.println(" Match scheduling failed.");
                }
            }

        } catch (Exception e) {
            System.out.println(" Something went wrong. Please check your input.");
        }
    }

    private boolean isValidTeam(Connection con, int teamId) throws SQLException {
        String query = "SELECT team_id FROM teams WHERE team_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, teamId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    private boolean isValidTournament(Connection con, int tournamentId) throws SQLException {
        String query = "SELECT tournament_id FROM tournaments WHERE tournament_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, tournamentId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void getDate() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter Match Date (YYYY-MM-DD): ");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("Date cannot be empty, Please enter a valid Date:");
                        attempt++;

                    }

                    if (input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                        date = input;
                        getTournament();
                        return;
                    }

                    else {
                        System.out.println("Invalid Date ! please follow This format (YYYY-MM-DD)!");
                    }
                    attempt++;
                } catch (Exception e) {
                }

            }
            retryLogic(attempt);
        }
    }

    void getTournament() {
        System.out.print("Enter Tournament ID: ");
        int tournamentId = SafeInput.getInt(sc);
        try (Connection con = Databaseconnection.getConnection()) {

            if (!isValidTournament(con, tournamentId)) {
                System.out.println(" Invalid tournament ID.");
                return;
            }
            
            // Check if organizer is assigned to this tournament
            int organizerId = SessionManager.getOrganizerId();
            if (!OrganizerTournamentValidator.isOrganizerAssignedToTournament(organizerId, tournamentId)) {
                OrganizerTournamentValidator.showAccessDeniedMessage();
                return;
            }
            
            MatchSchedule.tournamentId = tournamentId;
        } catch (Exception e) {
            System.out.println("An error Occur :" + e.getMessage());

        }
        retryLogic(tournamentId);
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
                int choice = SafeInput.getInt(sc);
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
