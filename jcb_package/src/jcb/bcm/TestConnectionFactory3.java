package jcb.bcm;

import java.sql.*;

/**
 * This client class demonstrates the Basic Connection Management
 * (BCM pacjkage) using the data source name, user, password,
 * and database name.
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
public class TestConnectionFactory3 {

  // the only reference to the ConnectionManager
  static ConnectionManager cm = null;

  //Main method
  public static void main(String[] args){

    try{

      // get the only instance of ConnectionManager
      cm = cm.getInstance();


      // get oracle connection and override the default values
      Connection conn1 = cm.getConnection(ConnectionManager.DATA_SOURCE_ORACLE,
                                          "system", "gozal", "scorpian");
      System.out.println("oracle connection="+conn1);

      // get a connection to an Access database using JDBC-ODBC
      Connection conn2 = cm.getConnection(ConnectionManager.DATA_SOURCE_ODBC,
                                          null, null, "northwind");
      System.out.println("odbc connection="+conn2);

      // get a connection to MySQL database
      Connection conn3 = cm.getConnection(ConnectionManager.DATA_SOURCE_MYSQL,
                                          "root", "root", "tiger");
      System.out.println("odbc connection="+conn3);
    }
    catch(Exception e){
      // handle the exceptions
      e.printStackTrace();
    }
    finally{
      if (cm!=null) {
        try{
          cm.close();
        }
        catch(SQLException se){
          se.printStackTrace();
        }
      }

      // close all connections ...
    }

  }

}

