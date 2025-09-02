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
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter Tournament ID: ");
            if (!sc.hasNextInt()) {
                System.err.println("Invalid input. Please enter a valid number.");
                return;
            }
            int tournamentId = SafeInput.getInt(sc);
            if (tournamentId == -1) return;

            if (!isValidTournament(tournamentId)) {
                System.err.println(" Invalid Tournament ID: " + tournamentId);
                return;
            }
            generateSummaryReport(tournamentId);
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    boolean isValidTournament(int tournamentId) {
        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM tournaments WHERE tournament_id = ?")) {
            
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Database error while validating tournament: " + e.getMessage());
            return false;
        }
    }

    public void generateSummaryReport(int tournamentId) {
        try (Connection con = Databaseconnection.getConnection()) {

            int totalPlayers = getCountWithParam(con,
                    "SELECT COUNT(DISTINCT p.Player_Id) " +
                            "FROM players p " +
                            "JOIN teams t ON p.team_id = t.team_id " +
                            "WHERE t.tournament_id = ?", tournamentId);

            int totalCoaches = getCountWithParam(con,
                    "SELECT COUNT(DISTINCT coach_id) FROM teams WHERE tournament_id = ?", tournamentId);
            int totalOrganizers = getCount(con, "SELECT COUNT(*) FROM users WHERE role = 'organizer'");
            int totalAdmins = getCount(con, "SELECT COUNT(*) FROM users WHERE role = 'admin'");
            int totalTournaments = getCount(con, "SELECT COUNT(*) FROM tournaments");

            int totalMatches = getCountWithParam(con, "SELECT COUNT(*) FROM matches WHERE tournament_id = ?", tournamentId);
            int totalMatchWins = getCountWithParam(con,
                    "SELECT COUNT(*) FROM match_result WHERE winner_team_id IS NOT NULL AND tournament_id = ?", tournamentId);

            System.err.println("\n Tournament Summary Report:");
            System.err.println("+-----------------------------------------------------------+");
            System.err.printf("| %-20s | %-6d |\n", "Total Matches", totalMatches);
            System.err.printf("| %-20s | %-6d |\n", "Total Players", totalPlayers);
            System.err.printf("| %-20s | %-6d |\n", "Total Tournaments", totalTournaments);
            System.err.printf("| %-20s | %-6d |\n", "Total Coaches", totalCoaches);
            System.err.printf("| %-20s | %-6d |\n", "Total Organizers", totalOrganizers);
            System.err.printf("| %-20s | %-6d |\n", "Total Admins", totalAdmins);
            System.err.println("+-----------------------------------------------------------+");

            // Call PDF generation method here
            generatePDFReport(tournamentId, totalMatches, totalPlayers, totalCoaches, totalOrganizers, totalAdmins,
                    totalMatchWins);

        } catch (SQLException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private int getCountWithParam(Connection con, String query, int tournamentId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, tournamentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private int getCount(Connection con, String query) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void generatePDFReport(int tournamentId, int matches, int players, int coaches, int organizers, int admins,
            int totalMatchWins) {
        try (FileOutputStream fos = new FileOutputStream("Tournament_Report.pdf")) {
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            document.open();

            try {
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

                document.add(new Paragraph("Tournament Summary Report", titleFont));
                document.add(new Paragraph(" ")); // spacer
                document.add(new Paragraph("Generated on: " + java.time.LocalDateTime.now()));

                PdfPTable table = new PdfPTable(2);
                table.addCell("Category");
                table.addCell("Count");

                table.addCell("Total Matches");
                table.addCell(String.valueOf(matches));
                table.addCell("Total Players");
                table.addCell(String.valueOf(players));

                table.addCell("Total Coaches");
                table.addCell(String.valueOf(coaches));
                table.addCell("Total Organizers");
                table.addCell(String.valueOf(organizers));
                table.addCell("Total Admins");
                table.addCell(String.valueOf(admins));
                table.addCell("Total Match Results");
                table.addCell(String.valueOf(totalMatchWins));

                document.add(table);

                System.err.println(" PDF report generated: Tournament_Report.pdf");
                System.err.println("PDF saved at: " + new java.io.File("Tournament_Report.pdf").getAbsolutePath());
            } finally {
                document.close();
            }
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage().replaceAll("[\r\n]", ""));
        }
    }

    public static void main(String[] args) {
        GenerateReport reportGenerator = new GenerateReport();
        reportGenerator.getTournamentId();
    }
}