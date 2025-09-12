package player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Databaseconnection;

public class PlayerWallet {
    public void initializeWallet(Connection con,int Player_Id) {
        String query = "INSERT INTO wallet ( Player_Id, balance) VALUES (?, 2000.00)";
        try {
                PreparedStatement pstmt = con.prepareStatement(query) ;
            pstmt.setInt(1, Player_Id);
            int choose=pstmt.executeUpdate();
            if(choose>0)
            {
                System.out.println("Wallet initialized with ₹ 2000.00"  + " for Player ID: " + Player_Id);
            }
            else{
                System.out.println("Initialization Failed !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getBalance(Connection con,int Player_Id) {
        String query = "SELECT balance FROM wallet WHERE Player_Id = ?";
        try {
             PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Player_Id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; 
    }

    public boolean deductBalance(Connection con,int Player_Id, double amount) {
        if (getBalance(con,Player_Id) < amount) {
            System.out.println("Insufficient balance in wallet.");
            return false;
        }

        String query = "UPDATE wallet SET balance = balance - ? WHERE Player_Id = ?";
        try {
             PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, Player_Id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean refundBalance(Connection con,int Player_Id, double amount) {
        String query = "UPDATE wallet SET balance = balance + ? WHERE Player_Id = ?";
        try {
             PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, Player_Id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addMoneyToWallet(int Player_Id, double amount) {
        String query = "UPDATE wallet SET balance = balance + ? WHERE Player_Id = ?";
        try (Connection con = Databaseconnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, Player_Id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
