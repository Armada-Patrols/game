import java.sql.*;

public class AccessDatabase {

    //Method to retrieve credentials from the database and validate against input.
    public static boolean getCredentials(String username, String password) {
        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String pass = null;

        try {
            Class.forName("org.sqlite.JDBC"); //Loads the driver.
            c = DriverManager.getConnection("jdbc:sqlite:test.db"); //Establishes connection to the database.

            String sql = "SELECT password FROM USERS WHERE USERNAME = ?"; //SQL query to retrieve password for given username.
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pass = rs.getString("password"); //Get password from the result set.
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage()); 
            System.exit(0); 
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pass != null && pass.equals(password)) {
            return true; //Password matches.
        }
        return false; //Password does not match or user not found.
    }
}