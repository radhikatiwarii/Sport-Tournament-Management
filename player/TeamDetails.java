package player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import util.Databaseconnection;

public class TeamDetails {

    public void showTeamDetails(int playerId) {
        String query = "SELECT t.team_id, t.team_name,u.user_name as Player_name, t.coach_id, t.tournament_id, t.team_status, t.team_logo, t.total_championships "
                +
                "FROM teams t " +
                "JOIN players p  ON t.team_id = p.team_id " +
                "Join users u ON p.user_id = u.user_id " +
                "WHERE p.player_id = ? AND p.team_id IS NOT NULL";

        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("--- Your Team Details ---");
            System.out
                    .println(
                            "+----------------------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-8s| %-15s| %-20s| %-8s| %-13s| %-11s| %-10s| %-13s|\n",
                    "team_id", "Team_name", "Player_name", "coach_id", "tournament_id", "Team_status", "logo",
                    "championships");
            System.out
                    .println(
                            "+----------------------------------------------------------------------------------------------------------------+");
            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                int team_id = rs.getInt("team_id");
                String Team_name = rs.getString("team_name");
                String Player_name = rs.getString("Player_name");
                int coach_id = rs.getInt("coach_id");
                int tournament_id = rs.getInt("tournament_id");
                String status = rs.getString("team_status");
                InputStream logo = rs.getBinaryStream("team_logo");
                String logoStatus = (logo != null) ? "Available" : "Missing";

                int championships = rs.getInt("total_championships");
                System.out.printf("|%-8d| %-15s| %-20s| %-8d| %-13d| %-11s| %-10s| %-13d|\n",
                        team_id, Team_name, Player_name, coach_id, tournament_id, status, logoStatus, championships);
                System.out.println(
                        "+----------------------------------------------------------------------------------------------------------------+");
                if (logo != null) {
                    try {
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        byte[] data = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = logo.read(data)) != -1) {
                            buffer.write(data, 0, bytesRead);
                        }

                        byte[] imageBytes = buffer.toByteArray();

                        ImageIcon icon = new ImageIcon(imageBytes);
                        JLabel label = new JLabel(icon);

                        JFrame frame = new JFrame("Team Logo - " + Team_name);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.getContentPane().add(label);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);

                        logo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!hasResult) {
                System.out.println("no Team Details available yet!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}