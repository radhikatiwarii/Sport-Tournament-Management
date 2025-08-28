package admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import util.Databaseconnection;

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
    int attempt = 3;
    while (attempt > 0) {
      System.out.println("Enter your email id :");
      String email = sc.nextLine();
      System.out.println("___________________________________________________________");

      System.out.println("Password");
      String password = sc.nextLine();
      System.out.println("___________________________________________________________");

      if (verifyUser(email, password)) {
        System.out.println();
        System.out.println("Login Succesfull Welcome , " + email);
        System.out.println("___________________________________________________________");

        return;
      } else {
        attempt--;
        if (attempt > 0) {
          System.out.println("Login Failed ! Invalid information or you are not admin ,you have " + attempt + " attempts left ,Try Again");
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