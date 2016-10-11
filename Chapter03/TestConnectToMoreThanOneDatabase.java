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
 public class TestConnectToMoreThanOneDatabase {

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

    public static void main(String[] args) {

        Connection oracleConn = null;
        Connection mysqlConn = null;
        try {
            System.out.println("-- TestConnectToMoreThanOneDatabase begin --");

            oracleConn = getOracleConnection();
            mysqlConn = getMySqlConnection();
            System.out.println("oracleConn="+oracleConn);
            System.out.println("mysqlConn="+mysqlConn);

            // now, we may use oracleConn to access an Oracle database
            // and use mysqlConn to access a MySQL database

            //
            // use oracleConn and mysqlConn
            //
            System.out.println("-- TestConnectToMoreThanOneDatabase end --");
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