package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrganizerTournamentValidator {
    
    public static boolean isOrganizerAssignedToTournament(int organizerId, int tournamentId) {
        try (Connection con = Databaseconnection.getConnection()) {
            String query = "SELECT 1 FROM organizers WHERE organizer_id = ? AND tournament_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, organizerId);
            ps.setInt(2, tournamentId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("Error validating organizer assignment: " + e.getMessage());
            return false;
        }
    }
    
    public static void showAccessDeniedMessage() {
        System.err.println("Access Denied: You are not assigned to this tournament!");
        System.err.println("Please contact admin for tournament assignment.");
    }
}