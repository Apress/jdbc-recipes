package jcb.bcm;

import java.sql.*;


/**
 * This class extends the abstract ConnectionFactory
 * for connecting to an MySQL database.
 *
 *
 * @author Mahmoud Parsian
 *
 */
class MysqlConnectionFactory extends ConnectionFactory {

  public static final String MYSQL_DRIVER = "org.gjt.mm.mysql.Driver";
  public static final String MYSQL_URL_PREFIX = "jdbc:mysql://localhost/";

  public static final String DEFAULT_USER = "root";
  public static final String DEFAULT_PASSWORD = "root";
  public static final String DEFAULT_DATABASE = "tiger";

  private static MysqlConnectionFactory mcf= null;

  private MysqlConnectionFactory() {
    driver = MYSQL_DRIVER;
    jdbcURL = MYSQL_URL_PREFIX;
  }

  //Public method used to get the only instance of MysqlConnectionFactory.
  public static synchronized ConnectionFactory getInstance(){

    //If not initialized, do it here. Otherwise just return existing object.
    if(mcf==null) {
      mcf = new MysqlConnectionFactory();
    }

    return mcf;

  }

  //Overridden method to open a database connection
  public Connection getConnection() throws Exception{

    // configure the JDBC URL
    jdbcURL = jdbcURL + databaseName;

    return super.getConnection();

  }

}

