package player;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;
import util.Databaseconnection;
import util.InputUtil;
import util.SafeInput;
import util.SessionManager;
import util.Validation;

public class EventRegistration {
    static int Player_Id;
    static int user_id;
    static String address;
    static int age;
    static String specialization;
    static String player_status;
    static int match_played;
    static int goal_scored;
    static int tournament_id;
    static String player_role;

    Scanner sc = new Scanner(System.in);
    Validation validator = new Validation();
    OpeningClosingDate ocd = new OpeningClosingDate();

    void registerPlayer(int tournamentId) {
        if (!isRegistrationOpen(tournamentId)) {
            System.out.println("Registration is CLOSED for this tournament.");
            return;
        } else {
            getUserId();
        }
    }

    boolean playerWallet(Connection con) {

        try {
            PlayerWallet wallet = new PlayerWallet();
            double registrationFee = 500.00;
            wallet.initializeWallet(con, Player_Id);

            if (!wallet.deductBalance(con, Player_Id, registrationFee)) {
                System.out.println("Wallet transaction failed. Registration cannot proceed.");
                return false;
            }

            String query = "select Player_Id from players where Player_Id=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, Player_Id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int player_id = rs.getInt("Player_Id");

                System.out.println("Registration Successful! ₹" + registrationFee + " deducted from wallet for Player "
                        + player_id);
                System.out.println(
                        "________________________________________________________________________________");
                return true;
            }

            else {
                System.out.println("Registration failed! Refund initiated.");
                System.out.println("___________________________________________");

                wallet.refundBalance(con, Player_Id, registrationFee);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    void playerConnection() {
        try (Connection con = Databaseconnection.getConnection()) {
            con.setAutoCommit(false);

            String query = "insert into players(user_id,address,age,specialization,player_status,match_played,goal_scored,player_role)\n"
                    + //
                    "values(?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, user_id);
            ps.setString(2, address);
            ps.setInt(3, age);
            ps.setString(4, specialization);
            ps.setString(5, player_status);
            ps.setInt(6, match_played);
            ps.setInt(7, goal_scored);
            ps.setString(8, player_role);
            int choose = ps.executeUpdate();
            
            System.out.println("Insert executed. Rows affected: " + choose);

            if (choose > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    Player_Id = rs.getInt(1);
                    SessionManager.setPlayer_Id(Player_Id);
                    System.out.println("Registration fee is ₹500. Do you want to proceed? (yes/no)");
                    String userResponse = sc.nextLine().trim().toLowerCase();
           

                    if (!userResponse.equals("yes")) {
                        System.out.println("Registration cancelled by user.");
                        System.out.println("___________________________________________");
                        con.rollback();
                        return;
                    }
                    boolean walletSuccess = playerWallet(con);
                     if (walletSuccess) {
                        con.commit();
                        System.out.println("Registration Succesfull !");
                        System.out.println("_______________________________________________________________");
                    } else {
                        System.out.println("Registration failed due to wallet issue or user cancellation.");
                        con.rollback();
                    }
                    return;
                } else {
                    System.out.println("Registration failed !");
                    System.out.println("_______________________________________________________________");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
    }

    public boolean isRegistrationOpen(int tournamentId) {
        String query = "SELECT registration_opening_date, registration_closing_date FROM tournaments WHERE tournament_id = ?";

        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, tournamentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date openingDate = rs.getDate("registration_opening_date");
                Date closingDate = rs.getDate("registration_closing_date");
                LocalDate today = LocalDate.now();

                if (openingDate != null && closingDate != null) {
                    return !today.isBefore(openingDate.toLocalDate()) && !today.isAfter(closingDate.toLocalDate());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void retryLogic(int attempt) {
        System.out.println("You have tried 3 Times,");
        System.out.println("_______________________________________________________________");

        int reAttempt = 0;
        while (reAttempt < 3) {
            try {
                System.out.println("---------------------------");
                System.out.println("[1]. Try Again");
                System.out.println("[2]. exit");
                System.out.println("---------------------------");
                System.out.println("choose an option :");
                int choice = InputUtil.chooseInt(sc);
                sc.nextLine();
                switch (choice) {
                    case 1: {
                        return;
                    }
                    case 2: {
                        System.out.println("Exiting....");
                        System.out.println("_______________________________________________________________");

                        break;
                    }
                    default: {
                        System.out.println("Invalid Choice , Try again:");
                        System.out.println("_______________________________________________________________");
                        reAttempt++;
                        break;
                    }
                }
                if (reAttempt == 3) {
                    System.out.println("Invalid attemts ! Exitting...");
                    System.out.println("_______________________________________________________________");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Exception Occurs : " + e.getMessage());
                System.out.println("_______________________________________________________________");
                reAttempt++;
            }
        }
    }

    public void getUserId() {
        user_id = SessionManager.getUserId();
        getAddress();
    }

    public void getAddress() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your address :");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_____________________________________");

                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("address  cannot be empty, Please enter your address:");
                        System.out.println("_______________________________________________________________");
                        attempt++;
                    }

                    if (input.matches("^[A-Za-z0-9\\s,]+$")) {
                        address = input;
                        getAge();
                        return;
                    } else {
                        System.out.println("Invalid address ! please Enter your native address");
                        System.out.println("_______________________________________________________________");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("Error Occurs : " + e.getMessage());
                }
            }
            retryLogic(attempt);
        }
    }

    public void getAge() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your age :");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_______________________________________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("age cannot be empty, Please enter  your valid age:");
                        System.out.println("_______________________________________________________________");
                        attempt++;
                    } else if (input.matches("\\d{1,3}")) {
                        age = Integer.parseInt(input);
                        getSpecialization();
                        return;
                    } else {
                        System.out.println("Invalid age ! Enter in digit !");
                        System.out.println("_______________________________________________________________");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                    System.out.println("_______________________________________________________________");
                }
            }
            retryLogic(attempt);
        }
    }

    public void getSpecialization() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your specialization :");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.matches("^[A-Za-z\\s]+$")) {
                        specialization = input;
                        getplayerStatus();
                        return;
                    } else {
                        System.out.println("Invalid specialization ! enter in which game you are perfect");
                        System.out.println("_______________________________________________________________");

                    }
                    attempt++;
                }

                catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                    System.out.println("_______________________________________________________________");
                }
            }
            retryLogic(attempt);
        }
    }

    public void getplayerStatus() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your status :");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.matches("(?i)Active|Inactive")) {
                        player_status = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
                        getMatchPlayed();
                        return;

                    } else {
                        System.out.println("Invalid status ! only (active/inactive) are allowed");
                        System.out.println("_______________________________________________________________");
                    }
                    attempt++;
                }

                catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                    System.out.println("_______________________________________________________________");
                }
            }
            retryLogic(attempt);
        }
    }

    public void getMatchPlayed() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter no. of matches you played :");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_____________________________________");

                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.matches("^\\d+$")) {
                        match_played = Integer.parseInt(input);
                        getGoalScored();
                        return;
                    }

                    else {
                        System.out.println("Invalid match count ! please enter a number");

                    }

                    attempt++;
                }

                catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                    e.printStackTrace();
                }
            }
            retryLogic(attempt);
        }
    }

    public void getGoalScored() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your scored goal :");
                    String input = SafeInput.getLine(sc).trim();
                    System.out.println("_____________________________________");

                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.matches("^\\d+$")) {
                        goal_scored = Integer.parseInt(input);
                        getPlayerRole();
                        return;
                    } else {
                        System.out.println("Invalid score ! enter numeric value");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }
            }
            retryLogic(attempt);
        }
    }

    public void getPlayerRole() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.println("Enter your role :");
                    String input =SafeInput.getLine(sc).trim();
                    System.out.println("_______________________________________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }
                    if (input.isEmpty()) {
                        System.out.println("role cannot be empty, Please enter  your valid role:");
                        System.out.println("_______________________________________________________________");
                        attempt++;
                    } else if (input.matches("[A-Za-z\\s]+$")) {
                        player_role = input;
                        playerConnection();
                        return;
                    } else {
                        System.out.println("Invalid role ! Enter in String !");
                        System.out.println("_______________________________________________________________");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                    System.out.println("_______________________________________________________________");
                }
            }
            retryLogic(attempt);
        }
    }

    void viewTournamentDetails() {
        try (Connection con = Databaseconnection.getConnection()) {
            String query = "SELECT tournament_id, name  FROM tournaments";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("List of all Registered Tournament:");
            System.out.println(
                    "+----------------------------------+");
            System.out.printf("| %-13s| %-18s| \n",
                    "Tournament_id", "Tournament Name");
            System.out.println(
                    "+----------------------------------+");

            while (rs.next()) {
                int tournament_id = rs.getInt("tournament_id");
                String tournament_name = rs.getString("name");

                System.out.printf("| %-13d| %-18s|\n",
                        tournament_id, tournament_name);
            }
            System.out.println(
                    "+----------------------------------+");
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }
    
    private void updateTournamentRevenue(Connection con, int tournamentId, double registrationFee) {
        try {
            // Insert revenue entry for this registration
            String revenueQuery = "INSERT INTO revenue (tournament_id, source, amount, revenue_date, organizer_fee, refund_amount) " +
                                "VALUES (?, 'Player Registration', ?, CURDATE(), ?, 0)";
            
            // Calculate organizer fee (10% of registration fee)
            double organizerFee = registrationFee * 0.10;
            
            PreparedStatement revenuePs = con.prepareStatement(revenueQuery);
            revenuePs.setInt(1, tournamentId);
            revenuePs.setDouble(2, registrationFee);
            revenuePs.setDouble(3, organizerFee);
            
            int revenueRows = revenuePs.executeUpdate();
            if (revenueRows > 0) {
                System.out.println("✅ Revenue updated: ₹" + registrationFee + " (Organizer fee: ₹" + organizerFee + ")");
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Warning: Could not update revenue - " + e.getMessage());
            // Don't fail the registration if revenue update fails
        }
    }
}