package player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import util.Databaseconnection;
import util.InputUtil;
import util.SafeInput;

public class GiveFeedback {

    public void submitFeedback(int player_id) {
        Scanner sc = new Scanner(System.in);

        
        System.out.print("Enter Match ID: ");
        int match_id =InputUtil.chooseInt(sc);
        sc.nextLine(); 
       
        System.out.print("Enter your feedback (max 255 characters): ");
        String message = SafeInput.getLine(sc).trim();

        
        String query = "INSERT INTO feedback (player_id, match_id, message) VALUES (?, ?, ?)";

        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, player_id);
            stmt.setInt(2, match_id);
            stmt.setString(3, message);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println(" Thank you! Your feedback has been submitted successfully.");
            } else {
                System.out.println(" Feedback submission failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
