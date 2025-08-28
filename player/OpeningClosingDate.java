package player;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import util.Databaseconnection;

public class OpeningClosingDate {
    public boolean isRegistrationOpen(int tournamentId) {
        try (Connection con=Databaseconnection.getConnection()) {
            String query = "SELECT registration_opening_date, registration_closing_date FROM tournaments WHERE tournament_id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, tournamentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LocalDate openingDate = rs.getDate("registration_opening_date").toLocalDate();
                LocalDate closingDate = rs.getDate("registration_closing_date").toLocalDate();
                LocalDate today = LocalDate.now();
                
                return (today.isAfter(openingDate) || today.isEqual(openingDate)) && today.isBefore(closingDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // If registration dates are not found, consider it closed
    }
}