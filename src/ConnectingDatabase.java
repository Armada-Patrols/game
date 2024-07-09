import java.sql.*;

public class ConnectingDatabase {
   //Method to establish a connection to the SQLite database.
   public static void establishConnection() {
      Connection c = null;
      Statement stmt = null;
      try {
         Class.forName("org.sqlite.JDBC"); //Loads the driver.
         c = DriverManager.getConnection("jdbc:sqlite:test.db"); //Establishes connection to the database.
         stmt = c.createStatement(); 
         String sql = "CREATE TABLE IF NOT EXISTS USERS (" +
                      "USERNAME CHAR(50) PRIMARY KEY NOT NULL, " +
                      "PASSWORD CHAR(50) NOT NULL)"; //SQL query to create the table.
         stmt.executeUpdate(sql);
         stmt.close();
         c.close();
      } catch (Exception e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }
   }
}