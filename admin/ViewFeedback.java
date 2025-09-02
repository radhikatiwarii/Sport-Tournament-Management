package admin;

import util.Databaseconnection;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class ViewFeedback {
    void viewFeedback()
    {
       try (Connection con=Databaseconnection.getConnection()){
        Statement stmt=con.createStatement();

        String query="Select * from feedback";
        
        ResultSet rs=stmt.executeQuery(query);

         System.out.println("Here are the Feedback Data");
             System.out.println("+-------------------------------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-15s| %-15s| %-10s|  %-51s| %-21s|  \n", 
                              "Feedback_id", "Player_id", "Match_id", "Message", "Submitted_at");
            System.out.println("+-------------------------------------------------------------------------------------------------------------------------+");
            boolean hasResult=false;

            while (rs.next()) {
                hasResult=true;
                int Feedback_id=rs.getInt("Feedback_id");
                int Player_id=rs.getInt("player_id");
                int Match_id=rs.getInt("match_id");
                String Message=rs.getString("message");
                Timestamp Submitted_at=rs.getTimestamp("submitted_at");

                System.out.printf("|%-15d| %-15d| %-10d|  %-51s| %-21s| \n", 
                                  Feedback_id, Player_id, Match_id, Message, Submitted_at);
                System.out.println("+-------------------------------------------------------------------------------------------------------------------------+");
            } if (!hasResult) {
                System.out.println("Non Feedback  available yet.");
                System.out.println("Thank You!");
            }
            
            }
        
        catch (Exception e) {
        e.printStackTrace();
       } 
    }
}
