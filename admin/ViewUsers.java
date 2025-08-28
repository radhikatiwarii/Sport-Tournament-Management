package admin;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Databaseconnection;

public class ViewUsers {
    public static void viewUsers()
    {
        Scanner sc=new Scanner(System.in);
        try(Connection con=Databaseconnection.getConnection();
        Statement stmt=con.createStatement())
        {
            System.out.println("Enter Role to View !");
            String input=sc.nextLine();

            String query="Select * from users where role='"+input+"'";
            ResultSet rs=stmt.executeQuery(query);

            System.out.println("Members in "+input);
             System.out.println("+----------------------------------------------------------------------------------------------------+");
            System.out.printf("|%-8s| %-15s| %-30s| %-15s| %-12s| %-10s| \n", 
                              "User_ID", "Name", "Email_id", "Password", "Phone_No", "Role");
            System.out.println("+----------------------------------------------------------------------------------------------------+");
            boolean hasResult=false;

            while (rs.next()) {
                hasResult=true;
                int User_ID=rs.getInt("user_id");
                String Name=rs.getString("user_name");
                String Email_id=rs.getString("email");
                String Password=rs.getString("password");
                String Phone_No=rs.getString("phone_no");
                String Role=rs.getString("role");
               
                 System.out.printf("|%-8d| %-15s| %-30s| %-15s| %-12s| %-10s| \n", 
                                  User_ID, Name, Email_id, Password, Phone_No, Role);
                System.out.println("+----------------------------------------------------------------------------------------------------+");
            }
             if (!hasResult) {
                System.out.println("Non "+ input+ " available yet.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        
    }

