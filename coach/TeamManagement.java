package coach;

import util.Databaseconnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import util.InputUtil;

public class TeamManagement {
    Scanner sc = new Scanner(System.in);
    Connection con;

    void manageTeam(int coachUserId) {
        try {
            con = Databaseconnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int teamId = getTeamIdByCoach(coachUserId);
        if (teamId == -1) {
            System.out.println("No team assigned to this coach.");
            return;
        }
        while (true) {
            System.out.println("_________________________________________________________________________");
            System.out.println("---Team Management---");
            System.out.println("-----------------------------------------------");
            System.out.println("-----------------------------------------------");
            System.out.println("1. View Team Player");
            System.out.println("2. Assign Captain");
            System.out.println("3. Remove Player");
            System.out.println("4. Back");
            System.out.println("-----------------------------------------------");
            System.out.println("-----------------------------------------------");

            System.out.print("Choose option: ");
            int choice = InputUtil.chooseInt(sc);

            switch (choice) {
                case 1:
                    viewTeamPlayer(teamId);
                    break;
                case 2:
                    assignCaptain(teamId);
                    break;
                case 3:
                    removePlayer(teamId);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

        }
    }

    private int getTeamIdByCoach(int coachId) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT team_id FROM teams WHERE coach_id = ?");
            ps.setInt(1, coachId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("team_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void viewTeamPlayer(int teamId) {
        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT p.Player_id, u.user_name as Player_name FROM users u " +
                            "JOIN players p ON u.user_id = p.user_id " +
                            "WHERE p.team_id = ?");
            ps.setInt(1, teamId);
            ResultSet rs = ps.executeQuery();
            System.out.println("Team Player :");
            System.out.println("+--------------------------+");
            System.out.printf("|%-9s| %-15s| \n",
                    "Player_id", "Player_name");
            System.out.println("+--------------------------+");
            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                int Player_id = rs.getInt("Player_id");
                String Player_name = rs.getString("Player_name");

                System.out.printf("|%-9d| %-15s| \n",
                        Player_id, Player_name);
                System.out.println("+--------------------------+");
            }
            if (!hasResult) {
                System.out.println("None player is  available yet.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void assignCaptain(int teamId) {
        System.out.print("Enter Player ID to assign as Captain: ");
        int Player_id = sc.nextInt();
        try {
            PreparedStatement ps = con
                    .prepareStatement("UPDATE players SET player_role = 'Captain' WHERE Player_id = ?");
            ps.setInt(1, Player_id);
            ps.executeUpdate();
            System.out.println("Captain assigned successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removePlayer(int teamId) {
        System.out.print("Enter Player ID to remove: ");
        int player_id = sc.nextInt();
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE players SET team_id = NULL WHERE Player_id = ?");
            ps.setInt(1, player_id);
            ps.executeUpdate();
            System.out.println("Player removed from team.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}