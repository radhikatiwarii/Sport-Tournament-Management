package player;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Databaseconnection;



public class ViewSchedule {

    public  void viewSchedule() {
        try (Connection conn = Databaseconnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String sql = "SELECT match_id, tournament_id, team1_id, team2_id, match_date FROM matches";
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("\nMatch Schedule:");
            System.out.println("+---------------------------------------------------------+");
            System.out.printf("|%-5s| %-12s| %-10s| %-10s| %-12s| \n", 
                              "ID", "Tournament", "Team 1", "Team 2", "Match Date");
            System.out.println("+---------------------------------------------------------+");
            
            while (rs.next()) {
                int matchId = rs.getInt("match_id");
                int tournamentId = rs.getInt("tournament_id");
                int team1Id = rs.getInt("team1_id");
                int team2Id = rs.getInt("team2_id");
                String matchDate = rs.getString("match_date");

                System.out.printf("|%-5d| %-12d| %-10d| %-10d| %-12s| |\n", 
                                  matchId, tournamentId, team1Id, team2Id, matchDate);
                                 
              
                                  System.out.println("+---------------------------------------------------------+");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}