package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import util.Databaseconnection;
import util.SafeInput;
import util.SessionManager;
import util.OrganizerTournamentValidator;

public class ManageRevenue {
    Scanner sc = new Scanner(System.in);
    
    public void manage_revenue() {
        while (true) {
            System.out.println("=====================================");
            System.out.println("        REVENUE MANAGEMENT");
            System.out.println("=====================================");
            System.out.println("1. View Tournament Revenue");
            System.out.println("2. Add Revenue Entry");
            System.out.println("3. View Revenue Summary");
            System.out.println("4. Sync Registration Revenue");
            System.out.println("5. Back");
            System.out.println("=====================================");
            System.out.print("Choose an option: ");
            
            String choice = SafeInput.getLine(sc);
            if (choice == null) return;
            
            choice = choice.trim();
            
            switch (choice) {
                case "1":
                    viewTournamentRevenue();
                    break;
                case "2":
                    addRevenueEntry();
                    break;
                case "3":
                    viewRevenueSummary();
                    break;
                case "4":
                    syncRegistrationRevenue();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
    
    private void viewTournamentRevenue() {
        System.out.print("Enter Tournament ID: ");
        String input = SafeInput.getLine(sc);
        if (input == null) return;
        
        try {
            int tournamentId = Integer.parseInt(input.trim());
            int organizerId = SessionManager.getOrganizerId();
            
            // Validate organizer access
            if (!OrganizerTournamentValidator.isOrganizerAssignedToTournament(organizerId, tournamentId)) {
                OrganizerTournamentValidator.showAccessDeniedMessage();
                return;
            }
            
            displayTournamentRevenue(tournamentId);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid tournament ID! Please enter a number.");
        }
    }
    
    private void displayTournamentRevenue(int tournamentId) {
        String query = "SELECT r.revenue_id, r.source, r.amount, r.revenue_date, " +
                      "r.organizer_fee, r.refund_amount, t.name as tournament_name " +
                      "FROM revenue r " +
                      "JOIN tournaments t ON r.tournament_id = t.tournament_id " +
                      "WHERE r.tournament_id = ?";
        
        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, tournamentId);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("\n=== TOURNAMENT REVENUE DETAILS ===");
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No revenue records found for this tournament.");
                return;
            }
            
            // Get tournament name
            rs.next();
            String tournamentName = rs.getString("tournament_name");
            System.out.println("Tournament: " + tournamentName);
            System.out.println("Tournament ID: " + tournamentId);
            System.out.println();
            
            // Reset cursor
            rs.beforeFirst();
            
            System.out.format("+------------+---------------+----------+------------+---------------+-------------+%n");
            System.out.format("| Revenue ID | Source        | Amount   | Date       | Organizer Fee | Refund      |%n");
            System.out.format("+------------+---------------+----------+------------+---------------+-------------+%n");
            
            double totalRevenue = 0;
            double totalOrganizerFee = 0;
            double totalRefund = 0;
            
            while (rs.next()) {
                int revenueId = rs.getInt("revenue_id");
                String source = rs.getString("source");
                double amount = rs.getDouble("amount");
                String date = rs.getString("revenue_date");
                double organizerFee = rs.getDouble("organizer_fee");
                double refund = rs.getDouble("refund_amount");
                
                System.out.format("| %-10d | %-13s | %-8.2f | %-10s | %-13.2f | %-11.2f |%n",
                        revenueId, source, amount, date, organizerFee, refund);
                
                totalRevenue += amount;
                totalOrganizerFee += organizerFee;
                totalRefund += refund;
            }
            
            System.out.format("+------------+---------------+----------+------------+---------------+-------------+%n");
            System.out.printf("Total Revenue: %.2f | Organizer Fee: %.2f | Total Refund: %.2f%n", 
                            totalRevenue, totalOrganizerFee, totalRefund);
            System.out.printf("Net Revenue: %.2f%n", (totalRevenue + totalOrganizerFee - totalRefund));
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("Error fetching revenue data: " + e.getMessage());
        }
    }
    
    private void addRevenueEntry() {
        System.out.print("Enter Tournament ID: ");
        String tournamentInput = SafeInput.getLine(sc);
        if (tournamentInput == null) return;
        
        try {
            int tournamentId = Integer.parseInt(tournamentInput.trim());
            int organizerId = SessionManager.getOrganizerId();
            
            // Validate organizer access
            if (!OrganizerTournamentValidator.isOrganizerAssignedToTournament(organizerId, tournamentId)) {
                OrganizerTournamentValidator.showAccessDeniedMessage();
                return;
            }
            
            System.out.print("Enter revenue source (e.g., Registration, Sponsorship, Tickets): ");
            String source = SafeInput.getLine(sc);
            if (source == null) return;
            
            System.out.print("Enter amount: ");
            String amountInput = SafeInput.getLine(sc);
            if (amountInput == null) return;
            
            System.out.print("Enter organizer fee: ");
            String feeInput = SafeInput.getLine(sc);
            if (feeInput == null) return;
            
            double amount = Double.parseDouble(amountInput.trim());
            double organizerFee = Double.parseDouble(feeInput.trim());
            
            insertRevenueEntry(tournamentId, source.trim(), amount, organizerFee);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter valid numbers.");
        }
    }
    
    private void insertRevenueEntry(int tournamentId, String source, double amount, double organizerFee) {
        try (Connection con = Databaseconnection.getConnection()) {
            con.setAutoCommit(false);
            
            // Insert revenue entry
            String revenueQuery = "INSERT INTO revenue (tournament_id, source, amount, revenue_date, organizer_fee, refund_amount) " +
                                "VALUES (?, ?, ?, CURDATE(), ?, 0)";
            
            PreparedStatement revenuePs = con.prepareStatement(revenueQuery);
            revenuePs.setInt(1, tournamentId);
            revenuePs.setString(2, source);
            revenuePs.setDouble(3, amount);
            revenuePs.setDouble(4, organizerFee);
            
            int revenueRows = revenuePs.executeUpdate();
            
            // If source is registration, update organizers table revenue column
            if (source.equalsIgnoreCase("Registration") || source.toLowerCase().contains("registration")) {
                updateOrganizerRevenue(con, tournamentId, amount);
            }
            
            if (revenueRows > 0) {
                con.commit();
                System.out.println(" Revenue entry added successfully!");
                if (source.equalsIgnoreCase("Registration") || source.toLowerCase().contains("registration")) {
                    System.out.println(" Organizer revenue updated with registration fees!");
                }
            } else {
                con.rollback();
                System.out.println(" Failed to add revenue entry.");
            }
            
        } catch (Exception e) {
            System.err.println("Error adding revenue entry: " + e.getMessage());
        }
    }
    
    private void updateOrganizerRevenue(Connection con, int tournamentId, double registrationAmount) throws Exception {
        // Calculate total registration revenue for this tournament
        String totalQuery = "SELECT SUM(amount) as total_registration FROM revenue " +
                           "WHERE tournament_id = ? AND (source = 'Registration' OR source LIKE '%registration%')";
        
        PreparedStatement totalPs = con.prepareStatement(totalQuery);
        totalPs.setInt(1, tournamentId);
        ResultSet totalRs = totalPs.executeQuery();
        
        double totalRegistrationRevenue = 0;
        if (totalRs.next()) {
            totalRegistrationRevenue = totalRs.getDouble("total_registration");
        }
        
        // Update organizers table revenue column
        String updateQuery = "UPDATE organizers SET revenue_management = ? " +
                           "WHERE tournament_id = ? AND organizer_id = ?";
        
        PreparedStatement updatePs = con.prepareStatement(updateQuery);
        updatePs.setDouble(1, totalRegistrationRevenue);
        updatePs.setInt(2, tournamentId);
        updatePs.setInt(3, SessionManager.getOrganizerId());
        
        int updateRows = updatePs.executeUpdate();
        if (updateRows > 0) {
            System.out.println(" Total registration revenue for tournament: ₹" + totalRegistrationRevenue);
        }
    }
    
    private void viewRevenueSummary() {
        int organizerId = SessionManager.getOrganizerId();
        
        String query = "SELECT t.tournament_id, t.name, " +
                      "SUM(r.amount) as total_revenue, " +
                      "SUM(r.organizer_fee) as total_organizer_fee, " +
                      "SUM(r.refund_amount) as total_refund " +
                      "FROM tournaments t " +
                      "LEFT JOIN revenue r ON t.tournament_id = r.tournament_id " +
                      "JOIN organizers o ON t.tournament_id = o.tournament_id " +
                      "WHERE o.organizer_id = ? " +
                      "GROUP BY t.tournament_id, t.name";
        
        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setInt(1, organizerId);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("\n=== REVENUE SUMMARY FOR YOUR TOURNAMENTS ===");
            System.out.format("+---------------+-------------------------+----------+---------------+----------+----------+%n");
            System.out.format("| Tournament ID | Tournament Name         | Revenue  | Organizer Fee | Refund   | Net      |%n");
            System.out.format("+---------------+-------------------------+----------+---------------+----------+----------+%n");
            
            double grandTotal = 0;
            
            while (rs.next()) {
                int tournamentId = rs.getInt("tournament_id");
                String name = rs.getString("name");
                double revenue = rs.getDouble("total_revenue");
                double fee = rs.getDouble("total_organizer_fee");
                double refund = rs.getDouble("total_refund");
                double net = revenue + fee - refund;
                
                System.out.format("| %-13d | %-23s | %-8.2f | %-13.2f | %-8.2f | %-8.2f |%n",
                        tournamentId, name, revenue, fee, refund, net);
                
                grandTotal += net;
            }
            
            System.out.format("+---------------+-------------------------+----------+---------------+----------+----------+%n");
            System.out.printf("Grand Total Net Revenue: %.2f%n", grandTotal);
            System.out.println();
            
        } catch (Exception e) {
            System.err.println("Error fetching revenue summary: " + e.getMessage());
        }
    }
    
    private void syncRegistrationRevenue() {
        int organizerId = SessionManager.getOrganizerId();
        
        try (Connection con = Databaseconnection.getConnection()) {
            // Get all tournaments for this organizer and calculate registration revenue
            String query = "SELECT t.tournament_id, t.name, " +
                          "(SELECT COUNT(*) FROM players p WHERE p.tournament_id = t.tournament_id) as player_count " +
                          "FROM tournaments t " +
                          "JOIN organizers o ON t.tournament_id = o.tournament_id " +
                          "WHERE o.organizer_id = ?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, organizerId);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("\n=== SYNCING REGISTRATION REVENUE ===");
            
            while (rs.next()) {
                int tournamentId = rs.getInt("tournament_id");
                String tournamentName = rs.getString("name");
                int playerCount = rs.getInt("player_count");
                
                // Calculate registration revenue (₹500 per player)
                double registrationRevenue = playerCount * 500.0;
                
                if (registrationRevenue > 0) {
                    // Update organizers table
                    String updateQuery = "UPDATE organizers SET revenue_management = ? " +
                                       "WHERE tournament_id = ? AND organizer_id = ?";
                    
                    PreparedStatement updatePs = con.prepareStatement(updateQuery);
                    updatePs.setDouble(1, registrationRevenue);
                    updatePs.setInt(2, tournamentId);
                    updatePs.setInt(3, organizerId);
                    
                    int updated = updatePs.executeUpdate();
                    if (updated > 0) {
                        System.out.printf(" %s: %d players = ₹%.2f\n", 
                                        tournamentName, playerCount, registrationRevenue);
                    }
                } else {
                    System.out.printf(" %s: No players registered yet\n", tournamentName);
                }
            }
            
            System.out.println("\n Registration revenue sync completed!");
            
        } catch (Exception e) {
            System.err.println("Error syncing registration revenue: " + e.getMessage());
        }
    }
}