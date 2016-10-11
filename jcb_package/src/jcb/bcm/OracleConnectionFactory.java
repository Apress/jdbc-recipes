package jcb.bcm;

import java.sql.*;


/**
 * This class extends the abstract ConnectionFactory
 * for connecting to an Oracle database.
 *
 *
 * @author Mahmoud Parsian
 *
 */
class OracleConnectionFactory extends ConnectionFactory {

  public static final String ORACLE_DRIVER =
    "oracle.jdbc.driver.OracleDriver";
  public static final String ORACLE_URL_PREFIX =
    "jdbc:oracle:thin:@localhost:1521:";

  public static final String DEFAULT_USER = "system";
  public static final String DEFAULT_PASSWORD = "gozal";
  public static final String DEFAULT_DATABASE = "scorpian";

  private static OracleConnectionFactory oracleConnFactory = null;

  private OracleConnectionFactory() {
    jdbcURL = ORACLE_URL_PREFIX;
    driver = ORACLE_DRIVER;
  }

  /**
   * Return the only instance of OracleConnectionFactory.
   */
  public static synchronized ConnectionFactory getInstance() {

    /**
     * If oracleConnFactory is not initialized, then initialize it.
     * Otherwise just return existing object.
     */
    if (oracleConnFactory ==null) {
       oracleConnFactory = new OracleConnectionFactory();
    }

    return oracleConnFactory;
  }

  /**
   * This method overrides the super class's method
   * to open a database connection.
   */
  public Connection getConnection() throws Exception{

    // configure the JDBC URL
    jdbcURL = jdbcURL + databaseName;

    return super.getConnection();
  }

}

