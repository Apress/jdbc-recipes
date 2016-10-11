import java.util.*;
import java.io.*;
import java.sql.*;

import jcb.db.*;
import jcb.meta.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class TestValidityOfConnection {

    /**
     * Create an Oracle Connection...
     */
    public static Connection getOracleConnection() throws Exception {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:scorpian";
        String username = "octopus";
        String password = "octopus";
        Class.forName(driver);  // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /**
     * Create an MySQL Connection...
     */
    public static Connection getMySqlConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/tiger";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /**
     * Test Validity of a Connection
     * @param conn a JDBC connection object
     * @param vendor a database vedor: { "oracle", "mysql", ...}
     * @return true if a given connection object is a valid one;
     *  otherwise return false.
     * @throws Exception Failed to determine if a given connection is valid.
     */
    public static boolean isValidConnection(Connection conn,
                                            String vendor)
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
        //
        // for MySQL database:
        //      you may use the connection object
        //      with query of "select 1"; if the
        //      query returns the result, then it
        //      is a valid connection object.

        if (vendor.equalsIgnoreCase("mysql")) {
            return testConnection(conn, "select 1");
        }
        else if (vendor.equalsIgnoreCase("oracle")) {
            return testConnection(conn, "select 1 from dual");
        }
        else {
            // you may add additional vendors here.
            return false;
        }
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
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
        }

    }

    public static void main(String[] args) {

        Connection oracleConn = null;
        Connection mysqlConn = null;
        try {
            System.out.println("-- TestValidityOfConnection begin --");

            // get connection to an Oracle database
            oracleConn = getOracleConnection();
            System.out.println("oracleConn="+oracleConn);
            System.out.println(isValidConnection(oracleConn, "oracle"));

            // get connection to a MySQL database
            mysqlConn = getMySqlConnection();
            System.out.println("mysqlConn="+mysqlConn);
            System.out.println(isValidConnection(mysqlConn, "mysql"));

            System.out.println("databases are shuting down...");

            // sleep for 30 seconds and during this time
            // shutdown both Oracle and MySQL databases:
            Thread.sleep(30000);

            // test to see if the Oracle connection is valid?
            System.out.println("oracleConn="+oracleConn);
            System.out.println(isValidConnection(oracleConn, "oracle"));

            // test to see if the MySQL connection is valid?
            System.out.println("mysqlConn="+mysqlConn);
            System.out.println(isValidConnection(mysqlConn, "mysql"));

            System.out.println("-- TestValidityOfConnection end --");
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
            DatabaseUtil.close(oracleConn);
            DatabaseUtil.close(mysqlConn);
        }
    }

}