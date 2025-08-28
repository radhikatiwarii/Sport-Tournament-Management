package coach;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import util.Databaseconnection;

public class CoachInformation {
        void connection() {
                try (Connection con = Databaseconnection.getConnection()) {
                        String query = "SELECT c.coach_id, u.user_name as coach_name, c.user_id, c.fees as coach_fees, c.specialization, c.description, c.years_of_experience "
                                        + "FROM coaches c " + //
                                        "JOIN users u ON c.user_id = u.user_id;";

                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        System.out.println("Coach Information:");
                        System.out.println(
                                        "+---------------------------------------------------------------------------------------------------------------------------------------+");
                        System.out.printf(
                                        "| %-8s| %-15s | %-8s| %-8s | %-14s| %-45s| %-15s | \n",
                                        "coach_id", "coach_name", "user_id", "coach_fees", "specialization",
                                        "description",
                                        "years_of_experience");
                        System.out.println(
                                        "+---------------------------------------------------------------------------------------------------------------------------------------+");
                        boolean hasResult = false;
                        while (rs.next()) {
                                hasResult = true;
                                int coach_id = rs.getInt("coach_id");
                                String coach_name = rs.getString("coach_name");
                                int user_id = rs.getInt("user_id");
                                double coach_fees = rs.getDouble("coach_fees");
                                String specialization = rs.getString("specialization");
                                String description = rs.getString("description");
                                int years_of_experience = rs.getInt("years_of_experience");

                                System.out.printf(
                                                "| %-8d| %-15s | %-8d| %-8f| %-14s| %-45s| %-18d  |\n",
                                                coach_id, coach_name, user_id, coach_fees, specialization, description,
                                                years_of_experience);

                                System.out.println(
                                                "+---------------------------------------------------------------------------------------------------------------------------------------+");
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
