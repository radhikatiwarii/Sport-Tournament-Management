package coach;

import util.Databaseconnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Timestamp;
import util.InputUtil;

class StrategyMacking {
    Scanner sc = new Scanner(System.in);
    static String Title;
    static String Description;

    public void createStrategy(int coachId) {
        String query = "INSERT INTO strategies (coach_id, title, description) VALUES (?, ?, ?)";
        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, coachId);
            pstmt.setString(2, Title);
            pstmt.setString(3, Description);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "✅ Strategy Created!" : "❌ Failed to create strategy.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getStrategyTitle() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter Strategy Title: ");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("Strategy title cannot be empty, Please enter a valid title:");
                        attempt++;

                    }

                    if (input.matches("^[A-Z][a-zA-Z ]{2,49}$")) {
                        Title = input;
                        getStrategyDescription();
                        return;
                    }

                    else {
                        System.out.println("Invalid title ! Your First letter should be Capital!");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }
    }

    public void getStrategyDescription() {
        while (true) {
            int attempt = 0;
            while (attempt < 3) {
                try {
                    System.out.print("Enter Strategy Description: ");
                    String input = sc.nextLine().trim();
                    System.out.println("_____________________________________");
                    if (input.equalsIgnoreCase("Exit")) {
                        return;
                    }

                    if (input.isEmpty()) {
                        System.out.println("Strategy Description cannot be empty, Please enter a valid Description:");
                        attempt++;

                    }

                    if (input.matches("^[a-zA-Z0-9 ,.'\"()\\-]{10,300}$")) {
                        Description = input;
                        CoachLogin login = new CoachLogin();
                        int coachId = login.Login();

                        if (coachId != -1) {

                            createStrategy(coachId);
                            return;
                        }
                    } else {
                        System.out.println("Invalid Description , Minimum 10 Characters Needed !");
                    }
                    attempt++;
                } catch (Exception e) {
                    System.out.println("An error Occur :" + e.getMessage());
                }

            }
            retryLogic(attempt);
        }

    }

    public void viewStrategies(int coachId) {
        String query = "SELECT * FROM strategies WHERE coach_id = ?";
        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, coachId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("--- View Strategies ---");
            System.out.println("+---------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-15s| %-55s| %-25s|  \n",
                    "Title", "Description", "Created_at");
            System.out.println("+---------------------------------------------------------------------------------------------------+");

            boolean hasResult = false;
            while (rs.next()) {
                hasResult = true;
                String Title = rs.getString("title");
                String Description = rs.getString("description");
                Timestamp Created_at = rs.getTimestamp("created_at");

                System.out.printf("|%-15s| %-55s| %-25s| \n",
                        Title, Description, Created_at);
                System.out.println("+---------------------------------------------------------------------------------------------------+");
            }
            if (!hasResult) {
                System.out.println("None Strategy is  available yet.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void strategyMenu(int coachId) {
        while (true) {
            System.out.println("--- Strategy Menu ---");
            System.out.println("1. Create Strategy");
            System.out.println("2. View Strategies");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = InputUtil.chooseInt(sc);
            sc.nextLine();

            switch (choice) {
                case 1: {
                    getStrategyTitle();
                    break;
                }
                case 2: {
                    viewStrategies(coachId);
                    break;
                }
                case 3: {
                    System.out.println("Exiting Strategy Menu...");
                    return;
                }
                default: {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    private void retryLogic(int attempt)

    {
        System.out.println("You have tried 3 Times,");
        int reAttempt = 0;
        while (reAttempt < 3) {
            try {
                System.out.println("[1]. Try Again");
                System.out.println("[2]. exit");
                System.out.println("choose an option :");
                int choice = InputUtil.chooseInt(sc);
                sc.nextLine();
                switch (choice) {
                    case 1: {
                        return;
                    }
                    case 2: {
                        System.out.println("Exiting....");
                        return;
                    }
                    default: {
                        System.out.println("Invalid Choice , Try again:");
                        reAttempt++;
                        break;
                    }
                }
                if (reAttempt == 3) {
                    System.out.println("Invalid attempts ! Exitting...");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Exception Occurs : " + e.getMessage());
                reAttempt++;
            }
        }
    }

}