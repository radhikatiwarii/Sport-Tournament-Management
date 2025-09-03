package main;

import java.sql.Connection;
import util.Databaseconnection;

public class Main {

    public static void main(String[] args) {
        HomePage homePage = new HomePage();
        homePage.homepage();
        

        try (Connection con = Databaseconnection.getConnection()) {
            System.out.println("Database connected successfully !");
        } catch (java.sql.SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}