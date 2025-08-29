package coach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import util.Databaseconnection;
import util.InputUtil;

public class TrackPerformance {
    Scanner sc = new Scanner(System.in);

    public void recordPerformance() {
        try {
            System.out.print("Enter player ID: ");
            int playerId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter match ID: ");
            int matchId = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter goals scored: ");
            int score = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter assists made: ");
            int assists = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter fitness rating (1-10): ");
            int fitness = Integer.parseInt(sc.nextLine().trim());

            Connection con = Databaseconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO performance (player_id, match_id, score, assists, fouls) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, playerId);
            ps.setInt(2, matchId);
            ps.setInt(3, score);
            ps.setInt(4, assists);
            ps.setInt(5, fitness);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Performance recorded successfully!" : "Failed to record performance.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewPerformanceByPlayer() {
        try {
            System.out.print("Enter player ID to view performance: ");
            int playerId = Integer.parseInt(sc.nextLine().trim());

            Connection con = Databaseconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM  performance WHERE player_id = ?");
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("Performance Records:");
            while (rs.next()) {
                System.out.println("Match ID: " + rs.getInt("match_id"));
                System.out.println("Goals: " + rs.getInt("goals"));
                System.out.println("Assists: " + rs.getInt("assists"));
                System.out.println("Fitness Rating: " + rs.getInt("fitness_rating"));
                System.out.println("----------------------------------");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void performance() {
        while (true) {
            System.out.println("Track Performance");
            System.out.println("____________________________________________________");
            System.out.println("----------------------------------------------------");
            System.out.println("----------------------------------------------------");
            System.out.println("1. View Performance");
            System.out.println("2. Record Performance");
            System.out.println("3. Back");
            System.out.println("----------------------------------------------------");
            System.out.println("----------------------------------------------------");
            int Choose = InputUtil.chooseInt(sc);
            sc.nextLine();
            switch (Choose) {
                case 1: {
                    viewPerformanceByPlayer();
                    System.out.println("__________________________________________");
                    break;
                }
                case 2: {
                    recordPerformance();
                    break;
                }
                case 3: {
                    System.out.println(" Back !");
                    System.out.println("__________________________________________");
                    return;
                }
                default: {
                    System.out.println("Invalid Choice Try Again!");
                    break;
                }
            }
        }
    }
}