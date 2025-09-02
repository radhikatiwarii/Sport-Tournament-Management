package organizer;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Databaseconnection;

public class ViewMatchResult {

    public void viewResults() {
        try (Connection conn = Databaseconnection.getConnection();
                Statement stmt = conn.createStatement()) {

            String sql = "SELECT result_id,match_id, team1_id, team2_id, team1_score,team2_score, winner_team_id " +
                    "FROM match_result WHERE result_id is not null";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\nMatch Results:");
            System.out.println(
                    "+-----------------------------------------------------------------------------------------+");
            System.out.printf("|%-10s| %-8s| %-10s| %-10s| %-12s| %-12s| %-15s|\n",
                    "Result_id", "Match_id", "Team 1", "Team 2", "Team1_score ", "Team2_score", "Winner_team_id");
            System.out.println("+-----------------------------------------------------------------------------------------+");

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                int resultId = rs.getInt("result_id");
                int matchId = rs.getInt("match_id");
                int team1Id = rs.getInt("team1_id");
                int team2Id = rs.getInt("team2_id");
                String team1Score = rs.getString("team1_score");
                String team2Score = rs.getString("team2_score");
                int winnerId = rs.getInt("winner_team_id");

                System.out.printf("|%-10d| %-8d| %-10d| %-10d| %-12s| %-12s| %-15d|\n",
                        resultId, matchId, team1Id, team2Id, team1Score, team2Score, winnerId);
                System.out.println("+-----------------------------------------------------------------------------------------+");
            }

            if (!hasResults) {
                System.out.println("No match results available yet.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
