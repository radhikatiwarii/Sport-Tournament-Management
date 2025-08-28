package main;

import java.sql.Connection;
import util.Databaseconnection;

public class Main {

    public static void main(String[] args) {
        HomePage main = new HomePage();
        main.homepage();

        try (Connection con = Databaseconnection.getConnection()) {
            System.out.println("Database connected successfully !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}