import java.sql.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class CheckJDBCInstallation_Oracle {

    /**
     * Create an Oracle Connection...
     */
    public static Connection getConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:caspian";
        String username = "scott";
        String password = "tiger";
        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /**
     * Test Validity of JDBC Installation
     * @param conn a JDBC connection object
     * @return true if a given connection object is a valid one;
     *  otherwise return false.
     * @throws Exception Failed to determine if a given connection is valid.
     */
    public static boolean isValidConnection(Connection conn)
        throws Exception {

        if (conn == null) {
            // null connection object is not valid
            return false;
        }

        if (conn.isClosed()) {
            // closed connection object is not valid
            return false;
        }

        // here we have a connection object which is not null and
        // which is not closed, but it might be a defunct object
        // in order to determine whether it is a valid connection,
        // depends on the vendor of the database:
        //
        // for Oracle database:
        //      you may use the connection object
        //      with query of "select 1 from dual";
        //      if the query returns the result, then
        //      it is a valid connection object.



        //  if you need to determine if the connection
        //  is still valid, you should issue a simple
        //  query, such as "SELECT 1 from dual". The driver
        //  will throw an exception if the connection is
        //  no longer valid.
        return testConnection(conn, "select 1 from dual");
    }

    /**
     * Test Validity of a Connection
     * @param conn a JDBC connection object
     * @param query a sql query to test against database connection
     * @return true if a given connection object is a valid one;
     *  otherwise return false.
     */
    public static boolean testConnection(Connection conn,
                                         String query) {

        ResultSet rs = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            if (stmt == null) {
                return false;
            }

            rs = stmt.executeQuery(query);
            if (rs == null) {
                return false;
            }

            if (rs.next()) {
                // connection object is valid: we were able to
                // connect to the database and return something useful.
                return true;
            }

            // there is no hope any more for the validity
            // of the connection object
            return false;

        }
        catch(Exception e) {
            //
            // something went wrong: connection is bad
            //
            return false;
        }
        finally {
            // close database resources
            close(rs);
            close(stmt);
        }

    }

    public static void main(String[] args) {

        Connection conn = null;
        try {
            System.out.println("-- CheckJDBCInstallation_Oracle begin --");

            // get connection to a Oracle database
            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("valid connection = "+ isValidConnection(conn));
            System.out.println("-- CheckJDBCInstallation_Oracle end --");
        }
        catch(Exception e){
            // handle the exception
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            //
            // release database resources
            //
            close(conn);
        }
    }


    public static void close(java.sql.Connection conn) {
        if (conn != null) {
            try {
                if (conn.isClosed()) {
                    return;
                }
                else {
                    conn.close();
                }
            }
            catch(Exception e) {
                // ignore
            }
        }
    }


    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e) {
            //ignore
        }
    }

    public static void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (Exception e) {
            //ignore
        }
    }

}