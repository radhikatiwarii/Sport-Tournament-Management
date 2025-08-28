package admin;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import util.Databaseconnection;

public class AssignOrganizer {
    public static void assignOrganizer() {
        Scanner sc=new Scanner(System.in);
        try (Connection con = Databaseconnection.getConnection()) {
            System.out.print("Enter Tournament ID: ");
            int tournamentId = sc.nextInt();
            sc.nextLine(); 

            System.out.print("Enter Organizer ID to assign: ");
            int organizerId = sc.nextInt();
            sc.nextLine();

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
}
