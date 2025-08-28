package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.Databaseconnection;

public class DeclareMatchWinner {
    public void declareMatchWinner() {

        String query = "SELECT * FROM sport_tournament.matchwinner";

        try (Connection connection = Databaseconnection.getConnection()) {
            if (connection == null) {
                System.err.println("Database connection failed! Connection is null.");
                return;
            }
            System.out.println("Database connection successful!");

            try (PreparedStatement ps = connection.prepareStatement(query);
                    ResultSet rs = ps.executeQuery()) {

                System.out.println("Executing query: " + query);

                System.out.println("          Player List                 ");

                if (!rs.isBeforeFirst()) {
                    System.out.println("No players found in the database.");
                    return;
                }

                while (rs.next()) {
                    String matchId = rs.getString("match_id");
                    String team1Score = rs.getString("team_1_score");
                    String team2Score = rs.getString("team_2_score");
                    String winner = rs.getString("winner");

                    // System.out.println(
                    // matchId + ", " + team1Score + ", " + team2Score + ", " + winner);
                    System.out.format("+----------+--------------+--------------+----------+%n");
                    System.out.format("| Match ID | Team 1 Score | Team 2 Score | Winner   |%n");
                    System.out.format("+----------+--------------+--------------+----------+%n");
                    System.out.format("| %-8s | %-12s | %-12s | %-8s |%n",
                            matchId, team1Score, team2Score, winner);
                    System.out.format("+----------+--------------+--------------+----------+%n");

                }

                System.out.println("=======================================");
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
