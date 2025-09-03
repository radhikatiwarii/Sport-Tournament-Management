package organizer;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.Databaseconnection;
import util.SafeInput;

public class ViewOrganizerAssignedToTournament {
    Scanner sc = new Scanner(System.in);

    void viewOrganizerAssignedToTournament() {
        System.out.println("Enter Your Organizer Id");
        int organizer_id = SafeInput.getInt(sc);
        if (!isValidOrganizerId(organizer_id)) {
            System.out.println("Invalid Organizer ID. Please enter a valid Organizer ID.");
        } else {
            try (Connection con = Databaseconnection.getConnection()) {
                String query = "SELECT t.name, t.tournament_id " +
                        "FROM tournaments t " +
                        "JOIN organizers o ON o.tournament_id = t.tournament_id " +
                        "WHERE o.organizer_id = ?";

                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, organizer_id);
                ResultSet rs = ps.executeQuery();

                System.out.println("Tournaments Assigned to Organizer with ID " + organizer_id + ":");

                System.out.println(
                        "+------------------------------------------------+");
                System.out.printf(
                        "| %-15s| %-30s| \n",
                        "Tournament_id", "Tournament_name" );
                System.out.println(
                        "+------------------------------------------------+");

                while (rs.next()) {
                    String tournamentName = rs.getString("name");
                    int tournamentId = rs.getInt("tournament_id");
                    System.out.printf(
                            "| %-15d| %-30s|\n",
                            tournamentId, tournamentName);
                    System.out.println(
                            "+------------------------------------------------+");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private boolean isValidOrganizerId(int organizer_id) {

        try (Connection con = Databaseconnection.getConnection()) {
            String query = "SELECT organizer_id FROM organizers WHERE organizer_id = ?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, organizer_id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
