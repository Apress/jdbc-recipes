package jcb.bcm;

import java.sql.*;
import jcb.util.DatabaseUtil;

/**
 * This client demonstrates the Basic Connection Management
 * (BCM package) using the data source name.
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
public class TestConnectionFactory2 {

    // the only reference to the ConnectionManager
    static  ConnectionManager cm = null;

    // driver method
    public static void main(String[] args) {
        Connection conn1 = null;
        Connection conn2 = null;
        Connection conn3 = null;
        try {
            // get the only instance of ConnectionManager
            cm = cm.getInstance();

            // use default values for user, password, database
            conn1 = cm.getConnection(ConnectionManager.DATA_SOURCE_ORACLE);
            System.out.println("oracle connection="+conn1);

            // get a connection to MySQL database
            conn2 = cm.getConnection(ConnectionManager.DATA_SOURCE_MYSQL);
            System.out.println("mysql connection="+conn2);

            // get a connection to a JDBC-ODBC registered database
            // NOTE: the odbc data source is registered by
            // Microsoft's ODBC Data Source Administrator
            conn3 = cm.getConnection(ConnectionManager.DATA_SOURCE_ODBC);
            System.out.println("odbc connection="+conn3);
        }
        catch(Exception e){
            //handle the exception
            e.printStackTrace();
        }
        finally {
            // close all connections
            DatabaseUtil.close(cm);
            DatabaseUtil.close(conn1);
            DatabaseUtil.close(conn2);
            DatabaseUtil.close(conn3);
        }
    }
}
