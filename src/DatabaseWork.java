import java.sql.*;

public class DatabaseWork {
    //Method to insert a new user into the database.
    public static boolean insertingOperation(String username, String password) {
        Connection c = null;
        PreparedStatement pstmtCheck = null;
        PreparedStatement pstmtInsert = null;
        ResultSet rs = null;
        
        try {
            Class.forName("org.sqlite.JDBC"); //Loads the driver.
            c = DriverManager.getConnection("jdbc:sqlite:test.db"); //Establishes connection to the database.
            c.setAutoCommit(false); 

            // Check if the username already exists
            String sqlCheck = "SELECT 1 FROM USERS WHERE USERNAME = ?";
            pstmtCheck = c.prepareStatement(sqlCheck);
            pstmtCheck.setString(1, username);
            rs = pstmtCheck.executeQuery();

            if (rs.next()) {
                return false; //Username already exists.
            }

            //Insert new user into the USERS table.
            String sqlInsert = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?, ?);";
            pstmtInsert = c.prepareStatement(sqlInsert);
            pstmtInsert.setString(1, username);
            pstmtInsert.setString(2, password);
            pstmtInsert.executeUpdate();

            c.commit();
            return true; //Insertion successful.
        } catch (Exception e) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false; //Insertion failed.
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtCheck != null) pstmtCheck.close(); 
                if (pstmtInsert != null) pstmtInsert.close(); 
                if (c != null) c.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}