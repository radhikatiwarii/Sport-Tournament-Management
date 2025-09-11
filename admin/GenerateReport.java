package admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.SafeInput;
import java.util.Scanner;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileOutputStream;
import util.Databaseconnection;

public class GenerateReport {

    void getTournamentId() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Tournament ID (or 'back' to return): ");
        String input = SafeInput.getLine(sc);
        
        if (input == null || input.trim().equalsIgnoreCase("back")) {
            System.out.println("Returning to previous menu...");
            return;
        }
        
        int tournament_id;
        try {
            tournament_id = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a valid number.");
            return;
        }

        if (!isValidTournament(tournament_id)) {
            System.err.println("Invalid Tournament ID: " + tournament_id);
            return;
        }
        generateSummaryReport(tournament_id);
    }

    boolean isValidTournament(int tournament_id) {
        try (Connection con = Databaseconnection.getConnection();
                PreparedStatement ps = con
                        .prepareStatement("SELECT COUNT(*) FROM tournaments WHERE tournament_id = ?")) {

            ps.setInt(1, tournament_id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Database error while validating tournament: " + e.getMessage());
            return false;
        }
    }

    public void generateSummaryReport(int tournament_id) {
        try (Connection con = Databaseconnection.getConnection()) {
            // Get tournament basic info
            String tournamentInfo = getTournamentInfo(con, tournament_id);

            // Get statistics
            int totalPlayers = getCountWithParam(con,
                    "SELECT COUNT(DISTINCT p.Player_Id) " +
                            "FROM players p " +
                            "JOIN teams t ON p.team_id = t.team_id " +
                            "WHERE t.tournament_id = ?",
                    tournament_id);
            int totalTeams = getCountWithParam(con,
                    "SELECT COUNT(*) FROM teams WHERE tournament_id = ?", tournament_id);
            int totalCoaches = getCountWithParam(con,
                    "SELECT COUNT(DISTINCT coach_id) FROM teams WHERE tournament_id = ?", tournament_id);
            int totalMatches = getCountWithParam(con,
                    "SELECT COUNT(*) FROM matches WHERE tournament_id = ?", tournament_id);
            int completedMatches = 0; // Simplified - no completed matches tracking

            // Display console report
            displayConsoleReport(tournamentInfo, totalPlayers, totalTeams, totalCoaches, totalMatches,
                    completedMatches);

            // Generate PDF report
            generateEnhancedPDFReport(con, tournament_id, tournamentInfo, totalPlayers, totalTeams,
                    totalCoaches, totalMatches, completedMatches);

        } catch (SQLException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private String getTournamentInfo(Connection con, int tournament_id) throws SQLException {
        String query = "SELECT t.name, t.start_date, t.end_date, t.status " +
                "FROM tournaments t " +
                "WHERE t.tournament_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, tournament_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return String.format("Tournament: %s | Dates: %s to %s | Status: %s",
                            rs.getString("name"),
                            rs.getString("start_date"),
                            rs.getString("end_date"),
                            rs.getString("status"));
                }
            }
        }
        return "Tournament ID: " + tournament_id;
    }

    private void displayConsoleReport(String tournamentInfo, int players, int teams, int coaches, int matches,
            int completed) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                    TOURNAMENT REPORT");
        System.out.println("=".repeat(80));
        System.out.println(tournamentInfo);
        System.out.println("-".repeat(80));
        System.out.printf("| %-25s | %-10d |\n", "Total Teams", teams);
        System.out.printf("| %-25s | %-10d |\n", "Total Players", players);
        System.out.printf("| %-25s | %-10d |\n", "Total Coaches", coaches);
        System.out.printf("| %-25s | %-10d |\n", "Total Matches Scheduled", matches);
        System.out.printf("| %-25s | %-10d |\n", "Matches Completed", completed);
        System.out.printf("| %-25s | %-10d |\n", "Matches Pending", Math.max(0, matches - completed));
        System.out.println("-".repeat(80));
    }

    private int getCountWithParam(Connection con, String query, int tournamentId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                int result = rs.next() ? rs.getInt(1) : 0;
                return Math.max(0, result); // Ensure non-negative
            }
        }
    }

    private int getCount(Connection con, String query) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            int result = rs.next() ? rs.getInt(1) : 0;
            return Math.max(0, result); // Ensure non-negative
        }
    }

    private void generateEnhancedPDFReport(Connection con, int tournament_id, String tournamentInfo,
            int players, int teams, int coaches, int matches, int completed) {
        String fileName = "Tournament_" + tournament_id + "_Report.pdf";
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();

            try {
                // Title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

                document.add(new Paragraph("TOURNAMENT REPORT", titleFont));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Generated on: " + java.time.LocalDateTime.now()));
                document.add(new Paragraph(" "));

                // Tournament Details Table
                addTournamentDetailsTable(document, con, tournament_id, headerFont);
                document.add(new Paragraph(" "));

                // Statistics Table
                document.add(new Paragraph("Tournament Statistics", headerFont));
                PdfPTable statsTable = new PdfPTable(2);
                statsTable.addCell("Metric");
                statsTable.addCell("Count");
                statsTable.addCell("Total Teams");
                statsTable.addCell(String.valueOf(teams));
                statsTable.addCell("Total Players");
                statsTable.addCell(String.valueOf(players));
                statsTable.addCell("Total Coaches");
                statsTable.addCell(String.valueOf(coaches));
                statsTable.addCell("Matches Scheduled");
                statsTable.addCell(String.valueOf(matches));
                statsTable.addCell("Matches Completed");
                statsTable.addCell(String.valueOf(completed));
                statsTable.addCell("Matches Pending");
                statsTable.addCell(String.valueOf(Math.max(0, matches - completed)));
                document.add(statsTable);
                document.add(new Paragraph(" "));

                // Teams Details
                addTeamsDetails(document, con, tournament_id, headerFont);

                // Match Results
                addMatchResults(document, con, tournament_id, headerFont);

                System.out.println(" PDF report generated: " + fileName);
                System.out.println(" PDF saved at: " + new java.io.File(fileName).getAbsolutePath());
            } finally {
                document.close();
            }
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }

    private void addTournamentDetailsTable(Document document, Connection con, int tournamentId, Font headerFont)
            throws Exception {
        document.add(new Paragraph("Tournament Information", headerFont));
        String query = "SELECT t.name, t.start_date, t.end_date, t.status " +
                "FROM tournaments t " +
                "WHERE t.tournament_id = ?";

        PdfPTable tournamentTable = new PdfPTable(2);
        tournamentTable.addCell("Field");
        tournamentTable.addCell("Details");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tournamentTable.addCell("Tournament Name");
                    tournamentTable.addCell(rs.getString("name"));
                    tournamentTable.addCell("Start Date");
                    tournamentTable.addCell(rs.getString("start_date"));
                    tournamentTable.addCell("End Date");
                    tournamentTable.addCell(rs.getString("end_date"));
                    tournamentTable.addCell("Status");
                    tournamentTable.addCell(rs.getString("status"));

                } else {
                    tournamentTable.addCell("Tournament ID");
                    tournamentTable.addCell(String.valueOf(tournamentId));
                }
            }
        }
        document.add(tournamentTable);
    }

    private void addTeamsDetails(Document document, Connection con, int tournament_id, Font headerFont)
            throws Exception {
        document.add(new Paragraph("Teams Participating", headerFont));
        String query = "SELECT t.team_name, u.user_name as coach_name, " +
                "(SELECT COUNT(*) FROM players p WHERE p.team_id = t.team_id) as player_count " +
                "FROM teams t " +
                "LEFT JOIN coaches c ON t.coach_id = c.coach_id " +
                "LEFT JOIN users u ON c.user_id = u.user_id " +
                "WHERE t.tournament_id = ?";

        PdfPTable teamsTable = new PdfPTable(3);
        teamsTable.addCell("Team Name");
        teamsTable.addCell("Coach");
        teamsTable.addCell("Players");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, tournament_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    teamsTable.addCell(rs.getString("team_name"));
                    teamsTable.addCell(rs.getString("coach_name") != null ? rs.getString("coach_name") : "No Coach");
                    teamsTable.addCell(String.valueOf(rs.getInt("player_count")));
                }
            }
        }
        document.add(teamsTable);
        document.add(new Paragraph(" "));
    }

    private void addMatchResults(Document document, Connection con, int tournamentId, Font headerFont)
            throws Exception {
        document.add(new Paragraph("Match Results", headerFont));
        String query = "SELECT m.match_id, t1.team_name as team1, t2.team_name as team2, " +
                "m.match_date " +
                "FROM matches m " +
                "LEFT JOIN teams t1 ON m.team1_id = t1.team_id " +
                "LEFT JOIN teams t2 ON m.team2_id = t2.team_id " +
                "WHERE m.tournament_id = ?";

        PdfPTable matchTable = new PdfPTable(4);
        matchTable.addCell("Match ID");
        matchTable.addCell("Team 1");
        matchTable.addCell("Team 2");
        matchTable.addCell("Date");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    matchTable.addCell(String.valueOf(rs.getInt("match_id")));
                    matchTable.addCell(rs.getString("team1"));
                    matchTable.addCell(rs.getString("team2"));
                    matchTable.addCell(rs.getString("match_date"));

                    // Removed score column
                }
            }
        }
        document.add(matchTable);
    }
}