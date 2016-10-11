package jcb.bcm;

import java.sql.*;

/**
 * This client demonstrates the Basic Connection Management
 * (BCM package) using the data source name, user, password,
 * and database name. These values override the default values.
 *
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
public class TestConnectionFactory3 {

    // the only reference to the ConnectionManager
    static ConnectionManager cm = null;

    public static void main(String[] args){
        Connection conn1 = null;
        Connection conn2 = null;
        Connection conn3 = null;
        try{
            // get the only instance of ConnectionManager
            cm = cm.getInstance();

            // get Oracle connection and override the default values
            conn1 = cm.getConnection(ConnectionManager.DATA_SOURCE_ORACLE,
                  "scott", "tiger", "scorpian");
            System.out.println("oracle connection="+conn1);

            // get a connection to an Access database using JDBC-ODBC
            // NOTE: the odbc data source is registered by
            // Microsoft's ODBC Data Source Administrator
            conn2 = cm.getConnection(ConnectionManager.DATA_SOURCE_ODBC,
                  null, null, "northwind");
            System.out.println("odbc connection="+conn2);

            // get a connection to MySQL database
            conn3 = cm.getConnection(ConnectionManager.DATA_SOURCE_MYSQL,
                  "root", "root", "snipit");
            System.out.println("odbc connection="+conn3);
        }
        catch(Exception e){
            // handle the exceptions
            e.printStackTrace();
        }
        finally{
            // close all connections
            DatabaseUtil.close(cm);
            DatabaseUtil.close(conn1);
            DatabaseUtil.close(conn2);
            DatabaseUtil.close(conn3);
        }
    }
}
