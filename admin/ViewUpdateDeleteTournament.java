package admin;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import util.Databaseconnection;
import util.InputUtil;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewUpdateDeleteTournament {
    Scanner sc = new Scanner(System.in);

    void showOperation() {
        Scanner sc = new Scanner(System.in);
        int attempt = 3;
        try {
            while (true) {
                System.out.println("+-----------------------------------------+");
                System.out.println("|Choose a Number According to the Otions! |");
                System.out.println("+-----------------------------------------+");
                System.out.println("|1. Update Tournament                     |");
                System.out.println("|2. Delete Tournament                     |");
                System.out.println("|3. View Tournament                       |");
                System.out.println("|4. Back                                  |");
                System.out.println("+-----------------------------------------+");
                int choose = InputUtil.chooseInt(sc);

                switch (choose) {
                    case 1: {
                        ViewUpdateDeleteTournament vt = new ViewUpdateDeleteTournament();
                        vt.updateTournament();
                        return;
                    }
                    case 2: {
                        ViewUpdateDeleteTournament vt = new ViewUpdateDeleteTournament();
                        vt.deleteTournament();

                        break;
                    }
                    case 3: {
                        ViewUpdateDeleteTournament vt = new ViewUpdateDeleteTournament();
                        vt.viewTournament();

                        break;
                    }
                    case 4: {
                        System.out.println("Back");
                        return;
                    }
                    default: {

                        attempt--;
                        System.out.println("Invalid Choice Try Again! You have " + attempt + " attempts left");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid Input Please Enter a Number !");
            System.out.println("______________________________________________________________");
            sc.next();
        }
    }

    void updateTournament() {
        try (Connection con = Databaseconnection.getConnection()) {
            System.out.println("Enter column name to update ");
            String update_column = sc.nextLine();
            List<String> allowedColumns = Arrays.asList("name", "start_date", "end_date", "status", "max_allowed",
                    "organizer_id", "registration_opening_date", "registration_closing_date");
            if (!allowedColumns.contains(update_column)) {
                System.out.println("Invalid column name.");
                return;
            }

            System.out.println("Enter new Value  ");
            String new_value = sc.nextLine();

            System.out.println("Enter tournament id ");
            String condition_value = sc.nextLine();

            String query = "update tournaments set " + update_column + "= ? " + " where tournament_id =" + "?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, new_value);
            stmt.setString(2, condition_value);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                 System.out.println(" Tournament with ID " + condition_value + " Updated successfully.");
            } else {
                 System.out.println(" No tournament found with ID " + condition_value + ". Nothing was deleted.");
            }

            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void deleteTournament() {
        try (Connection con = Databaseconnection.getConnection()) {
            System.out.println("enter the id of the column you want to delete !");
            int tournament_id = sc.nextInt();
            sc.nextLine();

            String query = "delete from tournaments where tournament_id =?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, tournament_id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(" Tournament with ID " + tournament_id + " deleted successfully.");
            } else {
                System.out.println(" No tournament found with ID " + tournament_id + ". Nothing was deleted.");
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void viewTournament() {
        try (Connection con = Databaseconnection.getConnection()) {
            Statement stmt = con.createStatement();

            String query = "select * from tournaments";
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("Data of The Tournament Table ");
            System.out.println(
                    "+-------------------------------------------------------------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-15s| %-15s| %-10s| %-10s| %-8s| %-12s| %-12s| %-25s| %-25s| \n",
                    "Tournament_id", "Name", "Start_date", "end_date", "status", "Max_allowed", "Organizer_id",
                    "Registration_Openingdate", "Registration_Closingdate");
            System.out.println(
                    "+-------------------------------------------------------------------------------------------------------------------------------------------------------+");
            boolean hasResult = false;

            while (rs.next()) {
                hasResult = true;
                int Tournament_id = rs.getInt("tournament_id");
                String Name = rs.getString("name");
                String Start_date = rs.getString("start_date");
                String End_date = rs.getString("end_date");
                String Status = rs.getString("status");
                int Max_allowed = rs.getInt("max_allowed");
                int Organizer_id = rs.getInt("organizer_id");
                String Registration_Openingdate = rs.getString("registration_opening_date");
                String Registration_Closingdate = rs.getString("registration_closing_date");
                System.out.printf("|%-15d| %-15s| %-10s| %-10s| %-8s| %-12d| %-12d| %-25s| %-25s| \n",
                        Tournament_id, Name, Start_date, End_date, Status, Max_allowed, Organizer_id,
                        Registration_Openingdate, Registration_Closingdate);
                System.out.println(
                        "+-------------------------------------------------------------------------------------------------------------------------------------------------------+");
            }
            if (!hasResult) {
                System.out.println("Non tournament available yet.");
                System.out.println("Thank You!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
