package jcb.bcm;

import java.sql.*;



/**
 * Class to demonstrate the enhanced Factory Method
 *
 * @author Mahmoud Parsian
 *
 */
public class TestConnectionFactory {

  //Only reference to the ConnectionManager
  static public ConnectionManager cm = null;

  //Main method
  public static void main(String[] args){

    try{

      // get the only instance of ConnectionManager
      cm = cm.getInstance();

      //Create and close a connection to the Oracle database to
      //demonstrate that it works.
      Connection conn1 = cm.getConnection(ConnectionManager.DATA_SOURCE_ORACLE,
                                          "system", "gozal", "scorpian");
      System.out.println("oracle connection="+conn1);

      // use default calues
      Connection conn2 = cm.getConnection(ConnectionManager.DATA_SOURCE_ORACLE);
      System.out.println("oracle connection="+conn2);
      //cm.close();

      //cm = cm.getInstance();
      //Open a connection to an Access database using ODBC
      Connection conn3 = cm.getConnection(ConnectionManager.DATA_SOURCE_ODBC,
                                          null, null, "northwind");
      System.out.println("odbc connection="+conn3);

      // use default calues
      Connection conn4 = cm.getConnection(ConnectionManager.DATA_SOURCE_ORACLE);
      System.out.println("oracle connection="+conn4);

      //Open a connection to an Access database using ODBC
      Connection conn5 = cm.getConnection(ConnectionManager.DATA_SOURCE_ODBC);
      System.out.println("odbc connection="+conn5);

      //Open a connection to MySQL
      Connection conn6 = cm.getConnection(ConnectionManager.DATA_SOURCE_MYSQL);
      System.out.println("odbc connection="+conn6);

    //Catch all the relevant errors
    }
    catch(SQLException se){
      se.printStackTrace();
    }
    catch(Exception e){
      e.printStackTrace();
    //Use finally block to ensure database resources are closed
    }
    finally{
      if(cm!=null)
        try{
          cm.close();
        }
        catch(SQLException se){
          se.printStackTrace();
        }
    }

  }//end main()

}//end TestConnectionFactory

