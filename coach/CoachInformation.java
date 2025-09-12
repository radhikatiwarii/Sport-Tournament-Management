package coach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.Databaseconnection;
import util.SessionManager;

public class CoachInformation {
        
        void connection() {
                try (Connection con = Databaseconnection.getConnection()) {
                        int coach_id = SessionManager.getCoach_id();
                        String query = "SELECT " +
                                        "c.coach_id, " +
                                        "u.user_name AS coach_name, " +
                                        "c.user_id, " +
                                        "c.fees AS coach_fees, " +
                                        "c.specialization, " +
                                        "c.description, " +
                                        "c.years_of_experience " +
                                        "FROM " +
                                        "coaches c " +
                                        "JOIN " +
                                        "users u ON c.user_id = u.user_id " +
                                        "WHERE " +
                                        "c.coach_id= ?";

                        PreparedStatement ps = con.prepareStatement(query);
                        ps.setInt(1, coach_id);
                        ResultSet rs = ps.executeQuery();

                        System.out.println("Coach Information:");
                        System.out.println(
                                        "+------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
                        System.out.printf(
                                        "| %-8s| %-15s| %-8s| %-8s| %-14s| %-75s| %-15s| \n",
                                        "coach_id", "coach_name", "user_id", "coach_fees", "specialization",
                                        "description",
                                        "years_of_experience");
                        System.out.println(
                                        "+------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
                        boolean hasResult = false;
                        while (rs.next()) {
                                hasResult = true;
                                int Coach_id = rs.getInt("coach_id");
                                String coach_name = rs.getString("coach_name");
                                int User_id = rs.getInt("user_id");
                                double coach_fees = rs.getDouble("coach_fees");
                                String specialization = rs.getString("specialization");
                                String description = rs.getString("description");
                                int years_of_experience = rs.getInt("years_of_experience");

                                System.out.printf(
                                                "| %-8d| %-15s| %-8d| %-8f| %-14s| %-75s| %-18d|\n",
                                                Coach_id, coach_name, User_id, coach_fees, specialization, description,
                                                years_of_experience);

                                System.out.println(
                                                "+------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
                        }
                        if (!hasResult) {
                                System.out.println("no coach are logged in  yet !");
                        }
                        System.out.println(
                                        "______________________________________________________________________________________________________________________________________________");
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

}
