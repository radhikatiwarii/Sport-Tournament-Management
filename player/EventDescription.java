package player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.Databaseconnection;

public class EventDescription {

    void viewEventDetails() {
        try (Connection con = Databaseconnection.getConnection()) {
            String query = "select * from tournaments ";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("Event Description:");
            System.out.println("+--------------------------------------------------------------------------------------------------------------+");
            System.out.printf("| %-13s| %-18s| %-15s| %-15s| %-12s| %-12s| %-12s|\n",
                    "Tournament_id","Tournament Name", "Start Date", "End Date", "Status", "Max Teams", "Organizer");
            System.out.println("+--------------------------------------------------------------------------------------------------------------+");


            while (rs.next()) {
                int tournament_id=rs.getInt("tournament_id");
                String name = rs.getString("name");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String status = rs.getString("status");
                int maxAllowed = rs.getInt("Max_allowed");
                int organizerId = rs.getInt("organizer_id");

                System.out.printf("| %-13d| %-18s| %-15s| %-15s| %-12s| %-12d| %-12d|\n",
                       tournament_id,name, startDate, endDate, status, maxAllowed, organizerId);
            }

            System.out.println("+--------------------------------------------------------------------------------------------------------------+");
        }

        catch (Exception e) {
            System.out.println("Error :"+e.getMessage());
        }
    }
    
}