package player;

import java.sql.Connection;
import java.util.Scanner;

import util.Databaseconnection;

public class CheckPlayerBalance {
    Scanner sc = new Scanner(System.in);

    public void checkBalance(int playerId) {
        try (Connection con = Databaseconnection.getConnection()) {

            PlayerWallet wallet = new PlayerWallet();
            double balance = wallet.getBalance(con, playerId);
            System.out.println("Your current wallet balance is: ₹" + balance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAmount(int playerId) {
        System.out.println("Enter amount to add:");
        double amount = sc.nextDouble();
        PlayerWallet wallet = new PlayerWallet();
        if (wallet.addMoneyToWallet(playerId, amount)) {
            System.out.println("₹" + amount + " added to your wallet successfully!");
        } else {
            System.out.println("Failed to add money. Try again!");
        }
    }
}