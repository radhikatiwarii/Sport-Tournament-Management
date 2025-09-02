package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.Databaseconnection;

public class ManageRevenue {
    
    public void manage_revenue() {

        String query = "SELECT * FROM sport_tournament.manage_revenue";

        try (Connection connection = Databaseconnection.getConnection()) {
            if (connection == null) {
                System.err.println("Database connection failed! Connection is null.");
                return;
            }
            System.out.println("Database connection successful!");

            try (PreparedStatement ps = connection.prepareStatement(query);
                    ResultSet rs = ps.executeQuery()) {

                System.out.println("Executing query: " + query);

                System.out.println("          Player List                 ");

                if (!rs.isBeforeFirst()) {
                    System.out.println("No players found in the database.");
                    return;
                }

                System.out.format("+------------+--------------+--------+---------+--------------+--------+%n");
                System.out.format("| Revenue ID | Source       | Amount | Revenue | Organizer    | Refund |%n");
                System.out.format("+------------+--------------+--------+---------+--------------+--------+%n");

                while (rs.next()) {
                    String revenueid = rs.getString("revenue_id");
                    String source = rs.getString("source");
                    String amount = rs.getString("amount");
                    String revenue = rs.getString("revenue_date");
                    String organizer = rs.getString("organizer_fee");
                    String refund = rs.getString("refund_amount");

                    System.out.format("| %-10s | %-12s | %-6s | %-7s | %-12s | %-6s |%n",
                            revenueid, source, amount, revenue, organizer, refund);
                }
                
                System.out.format("+------------+--------------+--------+---------+--------------+--------+%n");

                System.out.println("=======================================");
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
