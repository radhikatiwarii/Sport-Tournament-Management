package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databaseconnection {
    private  static final String URL="jdbc:mysql://localhost/sport_tournament?useSSL=false";
    private static final String USER="root";
    private static final String PASSWORD="radhika123";
    
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
