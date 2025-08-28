package player;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import util.Databaseconnection;

public class CoachAssignToPlayer {

    public void assignCoachToPlayer(int playerId) {

        Scanner sc = new Scanner(System.in);

        try (Connection con = Databaseconnection.getConnection();) {

            String query = "SELECT coach_id, specialization, fees, description, years_of_experience FROM coaches";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("  Available Coaches:");
            System.out
                    .println(
                            "+-----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-8s| %-15s| %-8s| %-12s| %-50s|  \n",
                    "coach_id", "Sport", "Fees", "Experience", "Description");
            System.out
                    .println(
                            "+-----------------------------------------------------------------------------------------------------+");
            boolean hasResult = false;
            while (rs.next()) {
                hasResult = true;
                int coach_id = rs.getInt("coach_id");
                String sport = rs.getString("specialization");
                BigDecimal fees = rs.getBigDecimal("fees");
                String description = rs.getString("description");
                int Experience = rs.getInt("years_of_experience");
                System.out.printf("|%-8d| %-15s| %-8.2f| %-12d| %-50s|\n",
                        coach_id, sport, fees, Experience, description);
                System.out.println(
                        "+-----------------------------------------------------------------------------------------------------+");

            }
            if (!hasResult) {
                System.out.println("Coach id not found !");
            }

            System.out.print("  Enter Coach ID you want to assign: ");
            int coachId = sc.nextInt();

            PreparedStatement ps = con.prepareStatement("SELECT fees FROM coaches WHERE coach_id = ?");
            ps.setInt(1, coachId);
            ResultSet coachRs = ps.executeQuery();

            if (coachRs.next()) {
                double requiredFees = coachRs.getDouble("fees");

                System.out.print("  Enter the fees to assign this coach: ₹");
                double enteredFees = sc.nextDouble();

                // Step 4: Check fees
                if (enteredFees >= requiredFees) {
                    // Assign coach to player
                    PreparedStatement assignPs = con.prepareStatement(
                            "UPDATE players SET coach_id = ? WHERE player_id = ?");
                    assignPs.setInt(1, coachId);
                    assignPs.setInt(2, playerId);
                    int rowsUpdated = assignPs.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Coach assigned successfully!");
                    } else {
                        System.out.println(" Failed to assign coach. Please check player ID.");
                    }
                } else {
                    System.out.println(" Insufficient fees. Coach not assigned.");
                }
            } else {
                System.out.println("Invalid Coach ID.");
            }

        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }
}
