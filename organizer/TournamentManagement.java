package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
import java.util.Scanner;

import util.Databaseconnection;
import util.SessionManager;

public class TournamentManagement {
    Scanner sc = new Scanner(System.in);

    void viewTournamentDetails() {
        try (Connection con = Databaseconnection.getConnection()) {
            int organizerId = SessionManager.getOrganizerId();
            String query = "SELECT t.tournament_id, t.name ,t.start_date, t.end_date, t.status, t.Max_allowed, t.Organizer_id ,v.name AS venue_name " +
                          "FROM tournaments as t " +
                          "LEFT JOIN venue v ON t.venue_id = v.venue_id " +
                          "JOIN organizers o ON t.tournament_id = o.tournament_id " +
                          "WHERE o.organizer_id = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, organizerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("List of all Registered Tournament For organizer Id "+organizerId+" : ");
            System.out.println(
                    "+------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.printf("| %-13s| %-18s| %-15s| %-15s| %-12s| %-12s| %-12s| %-20s|\n",
                    "Tournament_id", "Tournament Name", "Start Date", "End Date", "Status", "Max Teams", "Organizer","venue_name");
            System.out.println(
                    "+------------------------------------------------------------------------------------------------------------------------------------+");

            while (rs.next()) {
                int tournament_id = rs.getInt("tournament_id");
                String name = rs.getString("name");
                String startDate = rs.getString("start_date");
                String endDate = rs.getString("end_date");
                String status = rs.getString("status");
                int maxAllowed = rs.getInt("Max_allowed");
                int dbOrganizerId = rs.getInt("organizer_id");
                String venue_name=rs.getString("venue_name");

                System.out.printf("| %-13d| %-18s| %-15s| %-15s| %-12s| %-12d| %-12d| %-20s|\n",
                        tournament_id, name, startDate, endDate, status, maxAllowed, dbOrganizerId,venue_name);
            }
            System.out.println(
                    "+------------------------------------------------------------------------------------------------------------------------------------+");
        }

        catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }

    }

}