package admin;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.Databaseconnection;
import util.InputUtil;

public class AssignOrganizer {
    public static void assignOrganizer() {
        Scanner sc = new Scanner(System.in);
        try (Connection con = Databaseconnection.getConnection()) {
            System.out.print("Enter Tournament ID: ");
            int tournamentId = InputUtil.chooseInt(sc);
            sc.nextLine();

            System.out.print("Enter Organizer ID to assign: ");
            int organizerId = InputUtil.chooseInt(sc);
              sc.nextLine();
            checkOrganizer(organizerId);
          

            String query = "UPDATE tournaments SET organizer_id = ? WHERE tournament_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, organizerId);
            ps.setInt(2, tournamentId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(" Organizer assigned successfully!");
            } else {
                System.out.println(" Failed to assign organizer. Check IDs.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     static void checkOrganizer(int organizerId) {
        try (Connection con = Databaseconnection.getConnection()) {

            String checkQuery = "SELECT * FROM Organizers where organizer_id=?";
            PreparedStatement checkPs = con.prepareStatement(checkQuery);
            checkPs.setInt(1, organizerId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                System.out.println("Organizer id is true");
            } else {
                System.out.println("Invalid Organizer ID. No such organizer found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
