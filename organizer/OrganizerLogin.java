package organizer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import util.Databaseconnection;
import util.SafeInput;
import util.UniversalInput;
import util.SessionManager;

public class OrganizerLogin {
  Scanner sc = new Scanner(System.in);

  private boolean verifyUser(String email, String password) {
    boolean isVerified = false;
    try (Connection con = Databaseconnection.getConnection()) {
      String query = "select u.user_id from users u where u.email=? and u.password=? and u.role='organizer'";

      PreparedStatement ps = con.prepareStatement(query);
      ps.setString(1, email);
      ps.setString(2, password);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        int userId = rs.getInt("user_id");
        // Get organizer_id from organizers table
        String orgQuery = "select organizer_id from organizers where user_id=?";
        PreparedStatement orgPs = con.prepareStatement(orgQuery);
        orgPs.setInt(1, userId);
        ResultSet orgRs = orgPs.executeQuery();
        if (orgRs.next()) {
          SessionManager.setOrganizerId(orgRs.getInt("organizer_id"));
          isVerified = true;
        }
      }
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
        OrganizerDashboard od = new OrganizerDashboard();
        od.showImplementationDashboard(sc);
        return;
      } else {
        attempt--;
        if (attempt > 0) {
          System.out.println("Login Failed ! Invalid information or you are not organizer ,you have " + attempt + " attempts left ,Try Again");
          System.out.println("_______________________________________________________________________________________");
        } else {
          System.out.println("Too many Invalid Attempts. Please Try Again Later.");
          System.out.println("Thank You......");
          OrganizerDashboard pd = new OrganizerDashboard();
          pd.showDashboard(sc);
          System.out.println("_______________________________________________________________________");
          break;
        }
      }
    }
  }
}