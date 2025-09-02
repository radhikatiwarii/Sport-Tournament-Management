package player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import util.Databaseconnection;
import util.InputUtil;

public class JoinTeam {
    Scanner sc = new Scanner(System.in);
    

    public void joinTeam(int playerId, int tournamentId) {
        System.out.println("Available Teams for Tournament ID: " + tournamentId);
        listTeams(tournamentId);

        System.out.println("Enter the Team ID you want to join:");
        int teamId = InputUtil.chooseInt(sc);

        if (!isTeamValid(teamId, tournamentId)) {
            System.out.println("Invalid Team ID! Please select a valid team.");
            return;
        }

        String updateQuery = "UPDATE players SET team_id = ? WHERE player_id = ?";
        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setInt(1, teamId);
            stmt.setInt(2, playerId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Successfully joined the team!");
            } else {
                System.out.println("Failed to join the team. Firstly Register YourSelf !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void listTeams(int tournamentId) {
        String query = "SELECT team_id, team_name FROM teams WHERE tournament_id = ?";
        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, tournamentId);
            ResultSet rs = stmt.executeQuery();
              System.out.println("+-------------------------+");
            System.out.printf("|%-8s| %-15s| \n",
                    "team_id", "team_name");
            System.out.println("+-------------------------+");

            while (rs.next()) {
                int Team_id=rs.getInt("team_id");
                String Team_name=rs.getString("team_name");
                System.out.printf("|%-8d| %-15s| \n",
                        Team_id, Team_name);
                System.out.println("+-------------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isTeamValid(int teamId, int tournamentId) {
        String query = "SELECT team_id FROM teams WHERE team_id = ? AND tournament_id = ?";
        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, teamId);
            stmt.setInt(2, tournamentId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

}