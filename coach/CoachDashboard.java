package coach;

 
import java.util.Scanner;

import util.SafeInput;

public class CoachDashboard {

    public void showDashboard(Scanner sc) {
        System.out.println("Welcome to Coach Dashboard!");
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
                        CoachRegistration pr = new CoachRegistration();
                        pr.getFullName();
                        return;
                    }
                    case 2: {
                        System.out.println("Login");
                        CoachLogin pl = new CoachLogin();
                        int coachId = pl.Login();
                        if (coachId != -1) {
                            showImplementationDashboard(sc, coachId); // pass it forward
                        } else {
                            System.out.println("Login failed!");
                        }

                        System.out.println("______________________________________________________________");
                        break;

                    }
                    case 3: {
                        System.out.println("Back");
                        System.out.println("_______________________________________________________________________");
                        return;
                    }
                    default: {

                        attempt--;
                        System.out.println("Invalid Choice Try Again! You have" + attempt + "attempts left");
                        System.out.println("______________________________________________________________");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid Input Please Enter a Number !");
                System.out.println("______________________________________________________________");
                attempt--;
            }
        }

    }

    void showImplementationDashboard(Scanner sc, int coachId) {
        while (true) {
            System.out.println("+--------------------------------------------------------+");
            System.out.println("|                     Coach Menu !                       |");
            System.out.println("+--------------------------------------------------------+");
            System.out.println("|1. Coach Information                                    |");
            System.out.println("|2. Strategy Making                                      |");
            System.out.println("|3. view Assigned player to coach                        |");
            System.out.println("|4. Manage team                                          |");
            System.out.println("|5. Practice Session                                     |");
            System.out.println("|6. Track performance                                    |");
            System.out.println("|7. Back                                                 |");
            System.out.println("+--------------------------------------------------------+");
            System.out.println("Choose an option What you Want :");
            int choice = SafeInput.getInt(sc);

            sc.nextLine();

            switch (choice) {
                case 1: {
                    CoachInformation ci = new CoachInformation();
                    ci.connection();
                    System.out.println("__________________________________________");
                    break;
                }
                case 2: {

                    if (coachId != -1) {
                        StrategyMacking sm = new StrategyMacking();
                        sm.strategyMenu(coachId);
                    }

                    break;
                }
                case 3: {
                    if (coachId != -1) {
                        AssignedPlayerToCoach view = new AssignedPlayerToCoach();
                        view.viewAssignedPlayers(coachId);
                    }

                    break;
                }
                  
                case 4: {
                    if (coachId != -1) {
                        TeamManagement tm = new TeamManagement();
                        tm.manageTeam(coachId);
                    }
                    break;
                }
                case 5: {
                    PracticeSessionManager manager = new PracticeSessionManager();
                manager.getSessionDate();
                    manager.markAttendance();
                    break;
                }
                case 6: {
                  TrackPerformance tf=new TrackPerformance();
                  tf.performance();
                    break;
                }
                
                case 7: {
                    System.out.println(" Back !");
                    System.out.println("__________________________________________");
                    return;
                }
                default: {
                    System.out.println("Invalid Choice Try Again!");
                    break;
                }
            }
        }
    }
}