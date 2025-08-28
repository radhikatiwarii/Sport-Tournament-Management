package admin;

import util.Databaseconnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewCoachAssignedToPlayer {
    void viewCoachAssignedToPlayer() {
        try (Connection con = Databaseconnection.getConnection()) {
            Statement stmt = con.createStatement();
            String query = "SELECT " +
                    "  p.player_id, " +
                    "  pu.user_name AS player_name, " +
                    "  c.coach_id, " +
                    "  cu.user_name AS coach_name " +
                    "FROM " +
                    "  players p " +
                    "JOIN users pu ON p.user_id = pu.user_id " +
                    "JOIN coaches c ON p.coach_id = c.coach_id " +
                    "JOIN users cu ON c.user_id = cu.user_id";

            ResultSet rs = stmt.executeQuery(query);
            System.out.println("+--------------------------------+");
            System.out.printf("|%-15s| %-15s|  \n",
                    "Player_name", "Coach_name");
            System.out.println("+--------------------------------+");

            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                String player_name = rs.getString("player_name");
                String coach_name = rs.getString("coach_name");
                System.out.printf("|%-15s| %-15s| \n",
                        player_name, coach_name);
                System.out.println("+--------------------------------+");
            }
            if (!hasResult) {
                System.out.println("None Player Assinged to the Coach");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
