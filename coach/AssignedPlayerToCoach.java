package coach;

import util.Databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignedPlayerToCoach {
    public void viewAssignedPlayers(int coachId) {
        String query = "SELECT p.Player_id, u.user_name as Player_name, p.age as Age, p.specialization as Sport\r\n" + //
                "FROM players p\r\n" + //
                "JOIN users u ON p.user_id = u.user_id\r\n" + //
                "WHERE p.coach_id = ?\r\n" + //
                "";

        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, coachId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("---Assigned Player To Coach---");
            System.out.println("+--------------------------------------------------+");
            System.out.printf("|%-9s| %-15s| %-10s| %-10s| \n",
                    "Player_id", "Player_name", "Age", "Sport");
            System.out.println("+--------------------------------------------------+");

            boolean hasResult = false;
            while (rs.next()) {
                hasResult = true;
                int Player_id = rs.getInt("Player_id");
                String Player_name = rs.getString("Player_name");
                int Age = rs.getInt("Age");
                String Sport = rs.getString("Sport");

                System.out.printf("|%-9d| %-15s| %-10d| %-10s| \n",
                        Player_id, Player_name, Age, Sport);
                System.out.println("+--------------------------------------------------+");
            }

            if (!hasResult) {
                System.out.println("No players assigned to you yet.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching players: " + e.getMessage());
        }
    }
}
