package main;

import util.Databaseconnection;
import util.SafeInput;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.NoSuchElementException;
import java.util.Scanner;

import admin.AdminDashboard;
import coach.CoachDashboard;
import organizer.OrganizerDashboard;
import player.PlayerDashboard;

public class HomePage {
  
    public void autoCloseTournaments() {
    try (Connection con = Databaseconnection.getConnection()) {
        String query = "UPDATE tournaments SET status = 'closed' WHERE end_date < CURRENT_DATE AND status = 'open'";
        PreparedStatement ps = con.prepareStatement(query);
        int updated = ps.executeUpdate();
    } catch (Exception e) {
e.printStackTrace();    }
}

    public void homepage() {
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                autoCloseTournaments();
                System.out.println();
              System.out.println("_________________________________________________________________________");
               System.out.println("Welcome To The Sport Tournament management System!");
                System.out.println("#---Welcome To The Sport Tournament management System!---#");
                System.out.println();
                System.out.println("+-----------------------------------+");
                System.out.println("|                                   |");
                System.out.println("| Choose your role!                 |");
                System.out.println("+-----------------------------------+");
                System.out.println("| 1. for  Player Enter 1!           |");
                System.out.println("+-----------------------------------+");
                System.out.println("| 2. for  Coach Enter 2!            |");
                System.out.println("+-----------------------------------+");
                System.out.println("| 3. for  Organizer Enter 3!        |");
                System.out.println("+-----------------------------------+");
                System.out.println("| 4. for Admin Enter 4!             |");
                System.out.println("+-----------------------------------+");
                System.out.println("| 5. Exit                           |");
                System.out.println("+-----------------------------------+");

                System.out.println("Choose an Option!");
                int choose =SafeInput.getInt(sc);

                sc.nextLine();

                switch (choose) {
                    case 1: {
                        System.out.println("#---Player DashBoard Opens!---#");
                        System.out.println("_______________________________________________");
                        new PlayerDashboard().showDashboard(sc);
                        break;
                    }
                    case 2: {
                        System.out.println("#---Coach DashBoard Opens!---#");
                        System.out.println("_______________________________________________");
                        new CoachDashboard().showDashboard(sc);
                        break;
                    }
                    case 3: {
                        System.out.println("#---Organizer DashBoard Opens!---#");
                        System.out.println("_______________________________________________");
                        new OrganizerDashboard().showDashboard(sc);
                        break;
                    }
                    case 4: {
                        System.out.println("#---Admin DashBoard Opens!---#");
                        System.out.println("_______________________________________________");
                        new AdminDashboard().showDashboard(sc);
                        break;
                    }
                    case 5: {
                        System.out.println("Exiting the System GoodBye....");
                        return;
                    }

                    default: {
                        System.out.println("Invalid Choise Try Again:");
                    }
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println(" Input Mismatched ,Exiting the program.......");
        } catch (Exception e) {
            System.out.println("Invalid Input :" + e.getMessage());
            return;
        } finally {
            sc.close();
        }
    }
}
