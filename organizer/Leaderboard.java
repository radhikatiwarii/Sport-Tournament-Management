package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
   import java.sql.*;

public class Leaderboard {
    private Connection conn;

    
    public void enterMatchResult(int matchId, int team1Id, int team2Id, int team1Score, int team2Score) {
        try {
            String winner = (team1Score > team2Score) ? "team1" : (team2Score > team1Score) ? "team2" : "draw";

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO match_results (match_id, team1_id, team2_id, team1_score, team2_score, winner) VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setInt(1, matchId);
            ps.setInt(2, team1Id);
            ps.setInt(3, team2Id);
            ps.setInt(4, team1Score);
            ps.setInt(5, team2Score);
            ps.setString(6, winner);
            ps.executeUpdate();

            updateTeamPoints(team1Id, team2Id, winner);
            System.out.println("✅ Match result entered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Update Team Points
    private void updateTeamPoints(int team1Id, int team2Id, String winner) throws SQLException {
        if (winner.equals("draw")) {
            addPoints(team1Id, 1);
            addPoints(team2Id, 1);
        } else if (winner.equals("team1")) {
            addPoints(team1Id, 3);
        } else if (winner.equals("team2")) {
            addPoints(team2Id, 3);
        }
    }

    private void addPoints(int teamId, int points) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE teams SET points = points + ? WHERE team_id = ?"
        );
        ps.setInt(1, points);
        ps.setInt(2, teamId);
        ps.executeUpdate();
    }

    // 3. Show Leaderboard
    public void showLeaderboard() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT team_name, points FROM teams ORDER BY points DESC");

            System.out.println("\n🏆 Leaderboard:");
            while (rs.next()) {
                System.out.println(rs.getString("team_name") + " - " + rs.getInt("points") + " pts");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Declare Winner
    public void declareWinner() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT team_name FROM teams ORDER BY points DESC LIMIT 1");

            if (rs.next()) {
                System.out.println("\n🎉 Tournament Winner: " + rs.getString("team_name"));
            } else {
                System.out.println("No winner yet.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}