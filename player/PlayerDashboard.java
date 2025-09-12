package player;

 
import util.SafeInput;
import util.SessionManager;

import java.util.Scanner;

public class PlayerDashboard {

    public void showDashboard(Scanner sc) {
        System.out.println("Welcome to Player Dashboard!");
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
            int choice =     SafeInput.getInt(sc);
            sc.nextLine();

            try {
                switch (choice) {
                    case 1: {
                        System.out.println("Register Here!");
                        PlayerRegistration pr = new PlayerRegistration();
                         pr.getFullName();
                        return;
                    }
                    case 2: {
                        System.out.println("Login");
                        PlayerLogin pl = new PlayerLogin();
                        pl.Login();
                       
                        break;
                    }
                    case 3: {
                        System.out.println("Back");
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

    void showImplementationDashboard(Scanner sc) {
        System.out.println("+-------------------------------------------------------+");
        System.out.println("|                  player Menu           !              |");
        while (true) {
            System.out.println("+-------------------------------------------------------+");
            System.out.println("|1.  Event Registration                                 |");
            System.out.println("|2.  Assign Coach To Player                             |");
            System.out.println("|3.  Event Description                                  |");
            System.out.println("|4.  View Schedule                                      |");
            System.out.println("|5.  Event registration Opening Date &cClosing Date     |");
            System.out.println("|6.  Check Player Wollet Balance                        |");
            System.out.println("|7.  Join a Team                                        |");
            System.out.println("|8.  View My Team Details                               |");
            System.out.println("|9.  View Match Results                                 |");
            System.out.println("|10. Give Feedback on Matches                           |");
            System.out.println("|11. Back                                               |");
            System.out.println("+-------------------------------------------------------+");

            System.out.println("Choose an option What you Want :");
            int choice =  SafeInput.getInt(sc);

            sc.nextLine();

            switch (choice) {
                case 1: {
                    EventRegistration er = new EventRegistration();
                    er.viewTournamentDetails();
                    System.out.print("Enter Tournament ID: ");
                    int tournamentId = sc.nextInt();
                    System.out.println("___________________________________________");
                    er.registerPlayer(tournamentId);
                    break;
                }
                
                case 2: {
                    System.out.println("Enter Player_id");
                    int player_id = sc.nextInt();
                    sc.nextLine();
                    CoachAssignToPlayer cap = new CoachAssignToPlayer();
                    cap.assignCoachToPlayer(player_id);
                    break;
                }
                case 3: {
                    EventDescription ed = new EventDescription();
                    ed.viewEventDetails();
                    break;
                }
                case 4: {
                    ViewSchedule vs = new ViewSchedule();
                    vs.viewSchedule();
                    break;
                }

                case 5: {
                    System.out.print("Enter Tournament ID: ");
                    int tournamentId = sc.nextInt();
                    System.out.println("___________________________________________");

                    OpeningClosingDate ocd = new OpeningClosingDate();

                    if (ocd.isRegistrationOpen(tournamentId)) {
                        System.out.println("Registration is OPEN for this tournament.");
                    } else {
                        System.out.println("Registration is CLOSED for this tournament.");
                    }
                    break;
                }

                case 6: {
                    try {
                        System.out.println("Enter your Player ID:");
                        int playerId = sc.nextInt();
                        System.out.println("___________________________________________");
                        System.out.println("");
                        CheckPlayerBalance cpb = new CheckPlayerBalance();
                        while (true) {
                            System.out.println("Choose an option:");
                            System.out.println("--------------------------------");
                            System.out.println("1. Check Balance");
                            System.out.println("2. Add Amount");
                            System.out.println("3. Back");
                            System.out.println("--------------------------------");
                            int choose = sc.nextInt();

                            if (choose == 1) {
                                cpb.checkBalance(playerId);
                            } else if (choose == 2) {
                                cpb.addAmount(playerId);
                            } else if (choose == 3) {
                                System.out.println("Returning to the main menu ! ");
                                break;
                            } else {
                                System.out.println("Invalid choice! Please select 1 or 2.");
                            }

                        }
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case 7: {
                    JoinTeam jt = new JoinTeam();
                    int player_id = SessionManager.getPlayer_Id();
                    System.out.println("___________________________________________");

                    System.out.println("Select tournament_id from the Tournaments table given below:");
                    jt.showAvailableTournaments();
                    System.out.println("Enter tournament Id");
                    int tournamentId = sc.nextInt();
                    System.out.println("___________________________________________");
                    jt.joinTeam(player_id, tournamentId);
                    break;
                }

                case 8: {
                    TeamDetails td = new TeamDetails();
                    System.out.print("Enter your Player ID: ");
                    int playerId = sc.nextInt();
                    System.out.println("___________________________________________");
                    td.showTeamDetails(playerId);
                    break;
                }
              
                case 9: {
                    ViewMatchResult vmr = new ViewMatchResult();
                    vmr.viewResults();
                    break;
                }

                case 10: {
                    GiveFeedback feedbackObj = new GiveFeedback();
                    System.out.print("Enter your Player ID: ");
                    int player_id = sc.nextInt();
                    System.out.println("___________________________________________");

                    feedbackObj.submitFeedback(player_id);
                    break;
                }
                case 11: {
                    System.out.println("Back");
                    System.out.println("___________________________________________");
                    return;
                }
                default: {
                    System.out.println("Invalid Choice Try Again!");
                    System.out.println("___________________________________________");
                    break;
                }
            }
        }
    }

}