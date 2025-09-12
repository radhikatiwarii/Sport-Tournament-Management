package organizer;
 
import util.SafeInput;

import java.util.Scanner;

public class OrganizerDashboard {

    public void showDashboard(Scanner sc) {
        System.out.println("Welcome to Organizer Dashboard!");
        int attempt = 3;
        while (attempt > 0) {
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");
            System.out.println(" Choose an option :");
            System.out.println(" 1. Register");
            System.out.println(" 2. Login");
            System.out.println(" 3. Back");
            System.out.println("--------------------------------------------");
            System.out.println("--------------------------------------------");
            int choice =SafeInput.getInt(sc);
            sc.nextLine();

            try {
                switch (choice) {
                    case 1: {
                        System.out.println("Register Here!");
                        OrganizerRegistration or = new OrganizerRegistration();
                        or.getFullName();
                        return;
                    }
                    case 2: {
                        System.out.println("Login");
                        OrganizerLogin ol = new OrganizerLogin();
                        ol.Login();
                        // Only show implementation dashboard if login was successful
                        // Login method should handle its own flow
                        break;
                    }
                    case 3: {
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
        while (true) {
            System.out.println("+-------------------------------------------------------+");
            System.out.println("|                  Organizer Menu                       |");
            System.out.println("+-------------------------------------------------------+");
            System.out.println("|1. View Organizer Assigned to tournament               |");
            System.out.println("|2. Tournamet Management                                |");
            System.out.println("|3. Venue management                                    |");
            System.out.println("|4. View Players                                        |");
            System.out.println("|5. Team creation                                       |");
            System.out.println("|6. Match Scheduling                                    |");
            System.out.println("|7. Play Match                                          |");
            System.out.println("|8. View Match Results                                  |");
            System.out.println("|9. Exit                                                |");
            System.out.println("+-------------------------------------------------------+");

            System.out.print("Choose an Option: ");

            try {
                int choice = SafeInput.getInt(sc);
                sc.nextLine();

                switch (choice) {
                    case 1:
                        ViewOrganizerAssignedToTournament vot = new ViewOrganizerAssignedToTournament();
                        vot.viewOrganizerAssignedToTournament();
                        break;

                    case 2:
                        TournamentManagement tm = new TournamentManagement();
                        tm.viewTournamentDetails();
                        break;

                    case 3:
                        VenueManagement vm = new VenueManagement();
                        vm.display();
                        break;

                    case 4:
                        ViewPlayers vp = new ViewPlayers();
                        vp.viewPlayers();
                        break;

                    case 5:
                        CreateTeam ct = new CreateTeam();
                        ct.getTeamName();
                        break;

                    case 6:
                        MatchSchedule ms = new MatchSchedule();
                        ms.scheduleMatch();
                        break;
                    case 7:
                        Match m = new Match();
                        m.teamId();
                        break;
                    case 8:
                        ViewMatchResult vmr = new ViewMatchResult();
                        vmr.viewResults();
                        break;
                    case 9:
                        System.out.println(" Exiting Organizer Menu...");
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
