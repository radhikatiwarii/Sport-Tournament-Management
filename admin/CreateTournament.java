package admin;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import util.Databaseconnection;
import util.InputUtil;
import util.SafeInput;

public class CreateTournament {
    public static void createTournament() {
        Scanner sc = new Scanner(System.in);
        try (Connection con = Databaseconnection.getConnection()) {
            System.out.println("Enter Tournament Name :");
            String name = SafeInput.getLine(sc).trim();

            System.out.println("Enter Tournament Start Date :");
            String start_date = SafeInput.getLine(sc).trim();

            System.out.println("Enter tournament End date :");
            String end_date = SafeInput.getLine(sc).trim();

            System.out.println("Max Teams Allowed :");
            int max_teams =InputUtil.chooseInt(sc);

            sc.nextLine();
            System.out.println("Registration Opening Date :");
            String registration_opening_date = sc.nextLine();

            System.out.println("Registration Closing Date :");
            String registration_closing_date = sc.nextLine();

            String query = "INSERT INTO tournaments (name, start_date, end_date, Max_allowed, registration_opening_date, registration_closing_date) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?)";
                     PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setDate(2, Date.valueOf(start_date));
            ps.setDate(3, Date.valueOf(end_date));
            ps.setInt(4, max_teams);
            ps.setDate(5, Date.valueOf(registration_opening_date));
            ps.setDate(6, Date.valueOf(registration_closing_date));

             int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Tournament created successfully!");
            } else {
                System.out.println(" Failed to create tournament.");
            }
        } catch (Exception e) {
            System.out.println("Error while creating tournament:");
            e.printStackTrace();
        }
        }
    }