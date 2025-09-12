package player;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import util.Databaseconnection;
import util.SafeInput;
 

public class CoachAssignToPlayer {
 Scanner sc = new Scanner(System.in);

    public void assignCoachToPlayer(int playerId) {

       
        try (Connection con = Databaseconnection.getConnection();) {

            String query = "SELECT c.coach_id,u.user_name as coach_name,  c.fees FROM coaches as c join users u on c.user_id=u.user_id";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("  Available Coaches:");
            System.out
                    .println(
                            "+-----------------------------------+");
            System.out.printf("|%-8s| %-15s| %-8s|  \n",
                    "coach_id", "Coach_name", "Fees" );
            System.out
                    .println(
                            "+-----------------------------------+");
            boolean hasResult = false;
            while (rs.next()) {
                hasResult = true;
                int coach_id = rs.getInt("coach_id");
                String coach_name = rs.getString("coach_name");
                BigDecimal fees = rs.getBigDecimal("fees"); 
                System.out.printf("|%-8d| %-15s| %-8.2f| \n",
                        coach_id, coach_name, fees );
                System.out.println(
                        "+-----------------------------------+");

            }
            if (!hasResult) {
                System.out.println("Coach id not found !");
            }

            System.out.print("  Enter Coach ID you want to assign: ");
            int coachId = SafeInput.getInt(sc);
            if (coachId == -1) {
                System.out.println("Operation cancelled.");
                return;
            }

            PreparedStatement ps = con.prepareStatement("SELECT fees FROM coaches WHERE coach_id = ?");
            ps.setInt(1, coachId);
            ResultSet coachRs = ps.executeQuery();

            if (coachRs.next()) {
                double requiredFees = coachRs.getDouble("fees");

               // Step 1: Check and deduct from wallet
if (PlayerWallet(con, playerId, requiredFees)) {
    // Step 2: Assign coach to player
    PreparedStatement assignPs = con.prepareStatement(
            "UPDATE players SET coach_id = ? WHERE player_id = ?");
    assignPs.setInt(1, coachId);
    assignPs.setInt(2, playerId);
    int rowsUpdated = assignPs.executeUpdate();

    if (rowsUpdated > 0) {
        System.out.println(" Coach assigned successfully!");
    } else {
        System.out.println(" Failed to assign coach. Please check player ID.");
    }
} else {
    System.out.println(" Insufficient wallet balance or transaction failed. Coach not assigned.");
}
            }
        }
            

        catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

      boolean PlayerWallet(Connection con,int Player_Id,double requiredFees) {

        try {
            PlayerWallet wallet = new PlayerWallet();
            
             
            if (!wallet.deductBalance(con, Player_Id, requiredFees)) {
                System.out.println("Wallet transaction failed. Coach Assigning cannot proceed.");
                return false;
            }

            String query = "select Player_Id from players where Player_Id=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, Player_Id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int player_id = rs.getInt("Player_Id");

                System.out.println("Coach Assigning Successful! " + requiredFees + " deducted from wallet for Player "
                        + player_id);
                System.out.println(
                        "________________________________________________________________________________");
                return true;
            }

            else {
                System.out.println("Coach Assigning Failed! Refund initiated.");
                System.out.println("___________________________________________");

                wallet.refundBalance(con, Player_Id, requiredFees);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    }

