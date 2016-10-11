// bcm = basic connection management
package jcb.bcm;

import java.sql.*;

/**
 * This abstract class defines interface for factory objects.
 * All specific factories (Oracle, MySQL, ODBC, ...) will extend
 * this class.
 *
 * @author Mahmoud Parsian
 *
 */
abstract class ConnectionFactory {

  // data structures that hold database specific information
  protected Connection conn = null;
  protected int dataSourceName = -1; // undefined data source name
  protected String user = null;
  protected String password = null;
  protected String driver = null;
  protected String jdbcURL = null;
  protected String databaseName = null;

  /**
   * close the database connection
   */
  public  void close() throws SQLException {

    // close the connection object
    if (conn!=null) {

      System.out.println("closing connection"+conn);
      conn.close();
      System.out.println("connection closed.");

      // prepare for garbage collection
      conn = null;
    }

  }

  /**
   * Get a reference to a Connection object.
   */
  public Connection getConnection() throws Exception{

    if (conn == null) {
      System.out.println("Connection not created. Creating a new connection...");
      createConnection();
    }
    else {
      System.out.println("Connection exists. Returning an existing connection...");
    }

    return conn;

  }

  //Private method to create connection.
  private void createConnection() throws Exception {

    // load a driver
    Class.forName(driver).newInstance();

    // create a new Connection object
    System.out.println("Connecting to " + dataSourceName + " database...");
    conn = DriverManager.getConnection(jdbcURL, user, password);
    System.out.println("Connection creation successful..");
  }

}

