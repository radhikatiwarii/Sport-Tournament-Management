package organizer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.Databaseconnection;

public class ViewPlayers {

        void viewPlayers() {
                try (Connection con = Databaseconnection.getConnection()) {
                        String query = "SELECT p.Player_id, u.user_name as player_name, p.user_id, p.team_id, p.address, p.age, \n"
                                        + //
                                        "       p.specialization, p.player_status, p.match_played, p.goal_scored,p.player_role\n"
                                        + //
                                        "FROM players p\n" + //
                                        "JOIN users u ON p.user_id = u.user_id;";

                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        System.out.println("Player Information:");
                        System.out.println(
                                        "+-------------------------------------------------------------------------------------------------------------------------------------------------+");
                        System.out.printf(
                                        "| %-10s| %-15s| %-8s| %-8s| %-12s| %-4s| %-15s| %-15s| %-13s| %-12s| %-12s| \n",
                                        "player_id", "player_name", "user_id", "team_id", "address", "age",
                                        "player_role",
                                        "specialization",
                                        "player_status", "match_played", "goal_scored");
                        System.out.println(
                                        "+-------------------------------------------------------------------------------------------------------------------------------------------------+");

                        boolean hasResult = false;
                        while (rs.next()) {
                                hasResult = true;
                                int player_id = rs.getInt("Player_id");
                                String player_name = rs.getString("player_name");
                                int user_id = rs.getInt("user_id");
                                int team_id = rs.getInt("team_id");
                                String address = rs.getString("address");
                                int age = rs.getInt("age");
                                String specialization = rs.getString("specialization");
                                String player_status = rs.getString("player_status");
                                int match_played = rs.getInt("match_played");
                                int goal_scored = rs.getInt("goal_scored");
                                String player_role = rs.getString("player_role");

                                System.out.printf(
                                                "| %-10d| %-15s| %-8d| %-8d| %-12s| %-4d| %-15s| %-15s| %-13s| %-12d| %-12d|\n",
                                                player_id, player_name, user_id, team_id, address, age, player_role,
                                                specialization, player_status,
                                                match_played, goal_scored);
                                System.out.println(
                                                "+-------------------------------------------------------------------------------------------------------------------------------------------------+");
                        }
                        if (!hasResult) {
                                System.out.println("There is None Information about Player !");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }
}