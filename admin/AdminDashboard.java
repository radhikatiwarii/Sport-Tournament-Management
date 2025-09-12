package admin;

import java.util.Scanner;
import main.HomePage;
import util.NavigationHelper;
import util.SafeInput;
import util.GlobalInputHandler;

public class AdminDashboard {

    public void showDashboard(Scanner sc) {
        NavigationHelper.pushPage(() -> showDashboard(sc));
        System.out.println("Welcome to Admin Dashboard!");
        int attempt = 3;
        while (attempt > 0) {
            System.out.println("--------------------------------------------");
            System.err.println("--------------------------------------------");
            System.err.println(" Choose an option :");
            System.err.println(" 1. Login");
            System.err.println(" 2. Back");
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");
            int choice = SafeInput.getInt(sc);
            sc.nextLine();

            try {
                switch (choice) {
                    
                    case 1: {
                        System.out.println("Login");
                        AdminLogin al = new AdminLogin();
                        al.Login();
                      
                        break;
                    }
                    case 2: {
                        System.out.println("Back");
                        System.out.println("_______________________________________________________________________");
                        return;
                    }
                    default: {

                        attempt--;
                        System.out.println("Invalid Choice Try Again! You have " + attempt + " attempts left");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid Input Please Enter a Number !");
                System.out.println("______________________________________________________________");
                sc.next();
            }
        }
    }

    public void showImplementationDashboard(Scanner sc) {
        NavigationHelper.pushPage(() -> showImplementationDashboard(sc));
        int attempt[] = { 0 };
        try {
            while (true) {

                System.out.println("\n+----------------------------------------------------------+");
                System.out.println("|                 Admin Menu !                             |");
                System.out.println("+----------------------------------------------------------+");
                System.err.println("|1. view Users                                             |");
                System.err.println("|2. View Coach Assigned to Player                          |");
                System.out.println("|3. Create Tournament                                      |");
                System.out.println("|4. Assign Organizer to Tournament                         |");
                System.out.println("|5. Manage Tournament                               |");
                System.err.println("|6. View Feedback                                          |");
                System.out.println("|7. Generate Report                                        |");
                System.err.println("|0. Logout                                                 |");
                System.out.println("+----------------------------------------------------------+");

                String option = GlobalInputHandler.getInputWithTrim(sc, "Enter your option: ");
                if (option == null) {
                    HomePage hp = new HomePage();
                    hp.homepage();
                    return;
                }

                switch (option) {
                    case "1":
                        ViewUsers.viewUsers();
                        break;
                    case "2":
                        ViewCoachAssignedToPlayer vp = new ViewCoachAssignedToPlayer();
                        vp.viewCoachAssignedToPlayer();
                        break;
                    case "3":
                    CreateTournament ct=new CreateTournament();
                       ct.getTournamentName();
                        break;
                    case "4":
                    AssignOrganizer ao=new AssignOrganizer();
                        ao.assignOrganizer();
                        break;
                    case "5":
                        ViewUpdateDeleteTournament vt = new ViewUpdateDeleteTournament();
                        vt.showOperation();
                        break;
                    case "6":
                        ViewFeedback vf = new ViewFeedback();
                        vf.viewFeedback();
                        break;
                    case "7":
                       GenerateReport g = new GenerateReport();
                        g.getTournamentId();
                        break;

                    case "0":
                        System.err.println("\n+----------------------------------+");
                        System.err.println("| You have been logged out.        |");
                        System.err.println("+----------------------------------+\n");
                        HomePage hp = new HomePage();
                        hp.homepage();
                        return;

                    default:
                        System.err.println("\n+--------------------------------+");
                        System.err.println("| Invalid Option. Try Again.    |");
                        System.err.println("+--------------------------------+\n");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}