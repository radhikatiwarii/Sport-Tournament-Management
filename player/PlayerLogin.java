package player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import util.Databaseconnection;
import util.SafeInput;
import util.SessionManager;
import util.UniversalInput;

public class PlayerLogin {
  Scanner sc = new Scanner(System.in);

  private int verifyUser(String email, String password) {
    int user_id = -1;
    try (Connection con = Databaseconnection.getConnection()) {
      String query = "select user_id from users where email=? and password=? and role='player'";

      PreparedStatement ps = con.prepareStatement(query);

      ps.setString(1, email);
      ps.setString(2, password);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        user_id = rs.getInt("user_id");
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return user_id;

  }

  void Login() {
    UniversalInput.pushStep(() -> Login());
    int attempt = 3;
    while (attempt > 0) {
      String email = UniversalInput.getInputTrim(sc, "Enter your email id: ");
      if (email == null) return; // Back pressed
      System.out.println("___________________________________________________________");

      String password = UniversalInput.getInputTrim(sc, "Password: ");
      if (password == null) {
        // Back pressed on password - go back to email input
        continue; // This will restart the loop and ask for email again
      }
      System.out.println("___________________________________________________________");

      int user_id = verifyUser(email, password);
      if (user_id != -1) {
        SessionManager.setUserId(user_id);
        System.out.println();
        System.out.println("Login Succesfull Welcome , " + email);
        System.out.println("Your User ID is: " + user_id);
        System.out.println("___________________________________________________________");
        
        // Show implementation dashboard after successful login
        PlayerDashboard pd = new PlayerDashboard();
        pd.showImplementationDashboard(sc);
        return;
      } else {
        attempt--;
        if (attempt > 0) {
          System.out.println("Login Failed ! Invalid information or you are not player ,you have " + attempt
              + " attempts left ,Try Again");
          System.out.println("_______________________________________________________________________________________");
        } else {
          System.out.println("Too many Invalid Attempts. Please Try Again Later.");
          System.out.println("Thank You......");
          PlayerDashboard pd = new PlayerDashboard();
          pd.showDashboard(sc);
          System.out.println("_______________________________________________________________________");
          break;
        }
      }
    }
  }
}