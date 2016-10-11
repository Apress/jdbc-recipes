import java.util.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class TestSystemProperty {

    /**
     * Create an JDBC/ODBC Connection...
     */
    public static Connection getJdbcOdbcConnection() throws Exception {
        //String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
        // note: northwind data source must be defined
        // using the ODBC Data Source Administrator
        String url = "jdbc:odbc:northwind";
        String username = "";
        String password = "";
        //Class.forName(driver);    // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /**
     * Create an Oracle Connection...
     */
    public static Connection getOracleConnection() throws Exception {
        //String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:scorpian";
        String username = "octopus";
        String password = "octopus";
        //Class.forName(driver);    // load Oracle driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    /**
     * Create an MySQL Connection...
     */
    public static Connection getMySqlConnection() throws Exception {
        //String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/tiger";
        String username = "root";
        String password = "root";
        //Class.forName(driver);    // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }


    public static void main(String[] args) {

        System.out.println("-- TestSystemProperty begin --");

        Connection oracleConn = null;
        Connection mysqlConn = null;
        Connection odbcConn = null;

        System.out.println("System.getProperty(\"jdbc.drivers\")="+
            System.getProperty("jdbc.drivers"));

        // try getting a MySQL connection
        try {
            mysqlConn = getMySqlConnection();
            System.out.println("mysqlConn="+mysqlConn);
        }
        catch(Exception e) {
            System.out.println("error 1111="+e.getMessage());
        }


        // try getting an ODBC connection again
        try {
            odbcConn = getJdbcOdbcConnection();
            System.out.println("mysqlConn="+odbcConn);
        }
        catch(Exception e) {
            System.out.println("error 2222="+e.getMessage());
        }

        System.out.println("-- TestSystemProperty end --");
    }

}