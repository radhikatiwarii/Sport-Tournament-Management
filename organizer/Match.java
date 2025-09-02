package organizer;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import util.Databaseconnection;
import util.InputUtil;

class Match extends Thread {
    Scanner sc = new Scanner(System.in);
    static int team1_id;
    static int team2_id;
    static int team1_score;
    static int team2_score;
    static int teamAStrength;
    static int teamBStrength;

    void playMatch() {
        team1_score = 0;
        team2_score = 0;

        Random rand = new Random();
        for (int round = 1; round <= 3; round++) {
            teamAStrength = rand.nextInt(7);
            teamBStrength = rand.nextInt(7);

            System.out.println("Round " + round + " is going to held");
            try {
                Thread.sleep(5000);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Round " + round + ":");
            System.out.println("Team A Score for round " + round + " : " + teamAStrength);
            System.out.println("Team B Score for round " + round + " : " + teamBStrength);

            if (teamAStrength > teamBStrength) {
                team2_score++;
                System.out.println("Team A wins this round with Score " + teamAStrength);
            } else if (teamBStrength > teamAStrength) {
                team1_score++;
                System.out.println("Team B wins this round with Score " + teamBStrength);
            } else {
                System.out.println("It's a tie!");
            }
            try {
                Thread.sleep(10000);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();
        }
        try {
            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Final Score:");
        System.out.println("Team A: " + team1_score);
        System.out.println("Team B: " + team2_score);
        if (team1_score > team2_score) {
            System.out.println("Team A Won The Match ");
        } else {
            System.out.println("Team B Won The Match ");
        }

    }

    public void enterMatchResult(int match_id) {
        try (Connection con = Databaseconnection.getConnection()) {
            String winner_team_id;
            if (team1_score > team2_score) {
                winner_team_id = String.valueOf(team1_id);
            } else if (team2_score > team1_score) {
                winner_team_id = String.valueOf(team2_id);
            } else {
                winner_team_id = "draw";
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO match_result (match_id,team1_id, team2_id, team1_score, team2_score, winner_team_id) VALUES (?,?, ?, ?, ?, ?)");
            ps.setInt(1, match_id);
            ps.setInt(2, team1_id);
            ps.setInt(3, team2_id);
            ps.setInt(4, team1_score);
            ps.setInt(5, team2_score);
            ps.setString(6, winner_team_id);
            ps.executeUpdate();

            System.out.println(" Match result entered for Match ID: " + match_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void teamId() {
        System.out.print("Enter Match ID: ");
        int matchId = InputUtil.chooseInt(sc);
        boolean matchExists = false;
        try (Connection con = Databaseconnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT team1_id, team2_id FROM matches WHERE match_id = ?");
            ps.setInt(1, matchId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                matchExists = true;
                team1_id = rs.getInt("team1_id");
                team2_id = rs.getInt("team2_id");
            } else {
                System.out.println(" Match ID not found in database.");
            }
            if (matchExists) {
                playMatch();
                enterMatchResult(matchId);
            } else {
                System.out.println("Please enter a valid Match ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}