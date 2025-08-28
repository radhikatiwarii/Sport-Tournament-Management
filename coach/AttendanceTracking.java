package coach;

import util.Databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AttendanceTracking {
    Scanner sc = new Scanner(System.in);

    public void markAttendance(int coachId) {
        String query = """
                    SELECT p.Player_id, u.user_name AS Player_name
                    FROM players p
                    JOIN users u ON p.user_id = u.user_id
                    WHERE p.coach_id = ?
                """;

        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, coachId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- Mark Attendance ---");
            while (rs.next()) {
                int Player_id = rs.getInt("Player_id");
                String Player_name = rs.getString("Player_name");
                String checkQuery = "SELECT COUNT(*) FROM attendance WHERE Player_id = ? AND date = CURDATE()";
                try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                    checkStmt.setInt(1, Player_id);
                    ResultSet checkRs = checkStmt.executeQuery();
                    checkRs.next();
                    if (checkRs.getInt(1) > 0) {
                        System.out.println(Player_name + " already marked for today.");
                        continue;
                    }
                }
                System.out.print(Player_name + " (Present/Absent): ");
                String status = sc.nextLine().trim();
                if (!status.equals("present") && !status.equals("absent")) {
                    System.out.println("Invalid input. Please enter 'Present' or 'Absent'.");
                    continue;
                }

                String insertQuery = "INSERT INTO attendance (Player_id, date, status) VALUES (?, CURDATE(), ?)";
                try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, Player_id);
                    insertStmt.setString(2, status);
                    insertStmt.executeUpdate();
                }
            }

            System.out.println(" Attendance marked successfully.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}