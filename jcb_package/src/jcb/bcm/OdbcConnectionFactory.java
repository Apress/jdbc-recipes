package jcb.bcm;

import java.sql.*;


/**
 * This class extends the abstract ConnectionFactory
 * for connecting to an ODBC database. We will use
 * jdbc/odbc bridge driver.
 *
 * @author Mahmoud Parsian
 *
 */
class OdbcConnectionFactory extends ConnectionFactory{

  public static final String ODBC_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
  public static final String ODBC_URL_PREFIX = "jdbc:odbc:";

  public static final String DEFAULT_USER = "";
  public static final String DEFAULT_PASSWORD = "";
  public static final String DEFAULT_DATABASE = "northwind";


  private static OdbcConnectionFactory ocf= null;

  private OdbcConnectionFactory() {
     driver = ODBC_DRIVER;
     jdbcURL = ODBC_URL_PREFIX;
  }

  //Public method used to get the only instance of OdbcConnectionFactory.
  public static synchronized ConnectionFactory getInstance(){

    //If not initialized, do it here. Otherwise return existing object.
    if (ocf==null) {
      ocf = new OdbcConnectionFactory();
    }

    return ocf;

  }

  //Overridden method to open a database connection
  public Connection getConnection() throws Exception{

    // configure the JDBC URL
    jdbcURL = jdbcURL + databaseName;

    return super.getConnection();
  }

}

