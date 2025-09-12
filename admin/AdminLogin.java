package admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import util.Databaseconnection;
import util.UniversalInput;

public class AdminLogin {
  Scanner sc = new Scanner(System.in);

  private boolean verifyUser(String email, String password) {
    boolean isVerified = false;
    try (Connection con = Databaseconnection.getConnection()) {
      String query = "select *from users where email=? and password=? and role='admin'";

      PreparedStatement ps = con.prepareStatement(query);

      ps.setString(1, email);
      ps.setString(2, password);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        isVerified = true;
      }
      ps.close();
      rs.close();
      con.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return isVerified;

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

      if (verifyUser(email, password)) {
        System.out.println();
        System.out.println("Login Succesfull Welcome , " + email);
        System.out.println("___________________________________________________________");
        
        // Show implementation dashboard after successful login
        AdminDashboard ad = new AdminDashboard();
        ad.showImplementationDashboard(sc);
        return;
      } else {
        attempt--;
        if (attempt > 0) {
          System.err.println("Login Failed ! Invalid information or you are not admin ,you have " + attempt + " attempts left ,Try Again");
          System.out.println("_______________________________________________________________________________________");
        } else {
          System.out.println("Too many Invalid Attempts. Please Try Again Later.");
          System.out.println("Thank You......");
          AdminDashboard ad = new AdminDashboard();
          ad.showDashboard(sc);
          System.out.println("_______________________________________________________________________");
          break;
        }
      }
    }
  }
}