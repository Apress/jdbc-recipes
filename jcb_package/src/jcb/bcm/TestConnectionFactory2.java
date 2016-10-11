package jcb.bcm;

import java.sql.*;

/**
 * This client class demonstrates the Basic Connection Management
 * (BCM pacjkage) using the data source name.
 *
 * @author Mahmoud Parsian
 *
 */
public class TestConnectionFactory2 {

   // the only reference to the ConnectionManager
   static  ConnectionManager cm = null;

  // driver method
  public static void main(String[] args) {

    try {

      // get the only instance of ConnectionManager
      cm = cm.getInstance();

      // use default values for user, password, database
      Connection conn1 = cm.getConnection(ConnectionManager.DATA_SOURCE_ORACLE);
      System.out.println("oracle connection="+conn1);

      // get a connection to MySQL database
      Connection conn2 = cm.getConnection(ConnectionManager.DATA_SOURCE_MYSQL);
      System.out.println("mysql connection="+conn2);

      // get a connection to a jdbc-odbc registered database
      Connection conn3 = cm.getConnection(ConnectionManager.DATA_SOURCE_ODBC);
      System.out.println("odbc connection="+conn3);
    }
    catch(Exception e){
      //handle the exception
      e.printStackTrace();
    }
    finally {
        if(cm!=null) {
            try{
              cm.close();
            }
            catch(SQLException se){
              se.printStackTrace();
            }
        }

        // close all connections
    }

  }

}

