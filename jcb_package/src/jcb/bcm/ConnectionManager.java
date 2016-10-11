package jcb.bcm;

import java.sql.*;

/**
 * This class manages database connections.
 * This is a "singleton" class.  A singleton
 * class is a class with only one object for
 * a given class and this object should be
 * easily accessible to clients.
 *
 * @author Mahmoud Parsian
 *
 */
class ConnectionManager {

  // Constants to represent several data sources
  // you may add additional data sources here.

  /**
   * Oracle data source representation.
   */
   public static final int DATA_SOURCE_ORACLE = 1;

  /**
   * MySQL data source representation.
   */
   public static final int DATA_SOURCE_MYSQL = 2;

  /**
   * odbc-registered  data source representation.
   */
   public static final int DATA_SOURCE_ODBC = 3;

  // the only instance of ConnectionManager class
  private static ConnectionManager theConnManager = null;

  // the specific connection factory
  private static ConnectionFactory theConnFactory = null;

  //Private constructor
  private ConnectionManager() {
  }

  /**
   * This method provides connection logic: it returns a Connection
   * based on the data source name. This method uses default user,
   * password, and databaseName.
   * @param dataSourceName the data source name
   * @return a jdbc Connection object
   * @throws Exception Failed to get a Connection object.
   */
  public Connection getConnection (int dataSourceName) throws Exception {

    // check the database vendor and assign the appropriate
    // factory to the theConnFactory, which is the base class type.
    switch(dataSourceName){

      // The specific factories are Singletons so get the only
      // instance and set the appropriate connection values.
      case DATA_SOURCE_ORACLE:
        theConnFactory = OracleConnectionFactory.getInstance();
        theConnFactory.dataSourceName = DATA_SOURCE_ORACLE;
        theConnFactory.databaseName = OracleConnectionFactory.DEFAULT_DATABASE;
        theConnFactory.user = OracleConnectionFactory.DEFAULT_USER;
        theConnFactory.password = OracleConnectionFactory.DEFAULT_PASSWORD;
        break;

      case DATA_SOURCE_MYSQL:
        theConnFactory = MysqlConnectionFactory.getInstance();
        theConnFactory.dataSourceName = DATA_SOURCE_MYSQL;
        theConnFactory.databaseName = MysqlConnectionFactory.DEFAULT_DATABASE;
        theConnFactory.user = MysqlConnectionFactory.DEFAULT_USER;
        theConnFactory.password = MysqlConnectionFactory.DEFAULT_PASSWORD;
        break;

      case DATA_SOURCE_ODBC:
        theConnFactory = OdbcConnectionFactory.getInstance();
        theConnFactory.dataSourceName= DATA_SOURCE_ODBC;
        theConnFactory.databaseName = OdbcConnectionFactory.DEFAULT_DATABASE;
        theConnFactory.user = OdbcConnectionFactory.DEFAULT_USER;
        theConnFactory.password = OdbcConnectionFactory.DEFAULT_PASSWORD;
        break;

      //
      // error handling for unsupported database types.
      //
      default:
        throw new SQLException("Database Vendor Not Supported");

    } //end switch


    // connect to the database and return reference.
    return theConnFactory.getConnection();
  }

  /**
   * This method provides connection logic: it returns a Connection
   * based on the data source name. This method uses default user,
   * password, and databaseName.
   * @param dataSourceName the data source name
   * @param user the database username
   * @param password the user's password
   * @param databaseName the data base name (desired database)
   * @return a jdbc Connection object
   * @throws Exception Failed to get a Connection object.
   */
  public Connection getConnection (int dataSourceName,
                                   String user,
                                   String password,
                                   String databaseName) throws Exception {

    // check the database vendor and assign the appropriate
    // factory to theConnFactory, which is the base class type.
    switch(dataSourceName){

      // The specific factories are Singletons so get the only
      // instance and set the appropriate connection values.
      case DATA_SOURCE_ORACLE:
        theConnFactory = OracleConnectionFactory.getInstance();
        theConnFactory.dataSourceName = DATA_SOURCE_ORACLE;
        break;

      case DATA_SOURCE_MYSQL:
        theConnFactory = MysqlConnectionFactory.getInstance();
        theConnFactory.dataSourceName = DATA_SOURCE_MYSQL;
        break;

      case DATA_SOURCE_ODBC:
        theConnFactory = OdbcConnectionFactory.getInstance();
        theConnFactory.dataSourceName= DATA_SOURCE_ODBC;
        break;

      //
      // error handling for unsupported database types.
      //
      default:
        throw new SQLException("Database Vendor Not Supported");

    }

    theConnFactory.user = user;
    theConnFactory.password = password;
    theConnFactory.databaseName = databaseName;

    // connect to the database and return reference.
    return theConnFactory.getConnection();
  }

  /**
   * close the database connection.
   */
   public void close() throws SQLException{
      theConnFactory.close();
   }

  /**
   * Return the only instance of the ConnectionManager.
   */
  public static synchronized ConnectionManager getInstance(){
     if(theConnManager==null) {
       theConnManager = new ConnectionManager();
     }

     return theConnManager;

  }

}
