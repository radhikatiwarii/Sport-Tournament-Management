package coach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import util.Databaseconnection;
import util.SafeInput;

public class CoachLogin {
  Scanner sc = new Scanner(System.in);

  private int verifyUser(String email, String password) {
    int coach_id=-1;
    try (Connection con = Databaseconnection.getConnection()) {
      String query = "SELECT c.coach_id\r\n" + //
                "FROM users u\r\n" + //
                "JOIN coaches c ON u.user_id = c.user_id\r\n" + //
                "WHERE u.email = ? AND u.password = ? AND u.role = 'coach'\r\n" + //
                "";

      PreparedStatement ps = con.prepareStatement(query);

      ps.setString(1, email);
      ps.setString(2, password);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
         coach_id=rs.getInt("coach_id");
      }
      ps.close();
      rs.close();
      con.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return coach_id;

  }

  int Login() {
    int attempt = 3;
    while (attempt > 0) {
      System.out.println("Enter your email id :");
      String email = SafeInput.getLine(sc).trim();

      System.out.println("Password");
      String password = SafeInput.getLine(sc).trim();

      int coach_id=verifyUser(email, password) ;
        if (coach_id != -1) {
            System.out.println("Login Successful! Welcome, " + email);
            return coach_id; 
        } else {
            attempt--;
            if (attempt > 0) {
                System.out.println("Login Failed! You have " + attempt + " attempts left.");
            } else {
                System.out.println("Too many invalid attempts. Try again later.");
            }
        }
    }
    return -1; 
    }
  }



