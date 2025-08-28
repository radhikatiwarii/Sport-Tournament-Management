package organizer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import util.Databaseconnection;

public class UpdateMatchResult {
    Scanner sc=new Scanner(System.in);
        public void updateMatchResult() {
        try {
            System.out.print("Enter the Match ID to update: ");
            int matchId = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter the Winner Team ID: ");
            int winnerTeamId = sc.nextInt();
            sc.nextLine();
            if (!isValidTeam(winnerTeamId)) {
                System.out.println(" Invalid Team ID. Please try again.");
                return;
            }

            System.out.print("Enter the Score (e.g., 2-1): ");
            String score = sc.nextLine().trim();

            if (!score.matches("^\\d+-\\d+$")) {
                System.out.println(" Invalid score format. Use the format: X-Y (e.g., 2-1).");
                return;
            }

            String query = "UPDATE matches SET winner_team_id = ?, score = ? WHERE match_id = ?";
            try (Connection connection = Databaseconnection.getConnection()) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, winnerTeamId);
                ps.setString(2, score);
                ps.setInt(3, matchId);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println(" Match result updated successfully!");
                } else {
                    System.out.println(" Failed to update match result. Please check the Match ID.");
                }
            }
        } catch (Exception e) {
            System.out.println(" Error occurred: " + e.getMessage());
        }
    }

    private static boolean isValidTeam(int teamId) {

        return teamId >= 1 && teamId <= 10;
    }
}


