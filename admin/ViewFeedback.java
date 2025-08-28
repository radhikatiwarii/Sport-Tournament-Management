package admin;

import java.util.Scanner;
import util.Databaseconnection;
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
             System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-15s| %-15s| %-10s| %-10s| %-51s| %-12s|  \n", 
                              "Feedback_id", "User_id", "Match_id", "Team_id", "Message", "Submitted_at");
            System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------------------------+");
            boolean hasResult=false;

            while (rs.next()) {
                hasResult=true;
                int Feedback_id=rs.getInt("Feedback_id");
                int User_id=rs.getInt("user_id");
                int Match_id=rs.getInt("match_id");
                int Team_id=rs.getInt("team_id");
                String Message=rs.getString("message");
                String Submitted_at=rs.getString("Submitted-at");

                System.out.printf("|%-15d| %-15d| %-10d| %-10d| %-51s| %-12s| \n", 
                                  Feedback_id, User_id, Match_id, Team_id, Message, Submitted_at);
                System.out.println("+-------------------------------------------------------------------------------------------------------------------------------------------------------+");
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
