import java.sql.*;
import jcb.util.DatabaseUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class SimpleProgramToAccessOracleDatabase {
   public static Connection getConnection()
      throws SQLException {
      String driver = "oracle.jdbc.driver.OracleDriver";
      // load the Oracle JDBC Driver
      Class.forName(driver);
      // define database connection parameters
      String dbURL = "jdbc:oracle:thin:@localhost:1521:scorpian";
      String dbUser = "octopus";
      String dbPassword = "octopus";
      // get a database connection object: A connection (session)
      // with a specific database. SQL statements are executed and
      // results are returned within the context of a connection.
      return DriverManager.getConnection(dbURL, dbUser, dbPassword);
   }

   public static void main(String[] args)
        throws SQLException {
        Connection conn = null ; // connection object
        Statement stmt = null;   // statement object
        ResultSet rs = null;     // result set object
        try {
            conn = getConnection(); // without Connection, can not do much
            // create a statement: This object will be used for executing
            // a static SQL statement and returning the results it produces.
            stmt = conn.createStatement();

            // start a transaction
            conn.setAutoCommit(false);

            // create a table called cats_tricks
            stmt.executeUpdate("CREATE TABLE cats_tricks " +
                "(name VARCHAR2(30), trick VARCHAR2(30))");
            // insert two new records to the cats_tricks table
            stmt.executeUpdate(
              "INSERT INTO cats_tricks VALUES('mono', 'rollover')" );
            stmt.executeUpdate(
              "INSERT INTO cats_tricks VALUES('mono', 'jump')" );

            // commit the transaction
            conn.commit();

            // set auto commit to true (from now on every single
            // statement will be treated as a single transaction
            conn.setAutoCommit(true) ;

            // get all of the the records from the cats_tricks table
            ResultSet rs = stmt.executeQuery(
                "SELECT name, trick FROM cats_tricks");

            // iterate the result set and get one row at a time
            while( rs.next() ) {
                 String name = rs.getString(1);  // 1st column in query
                 String trick = rs.getString(2); // 2nd column in query
                 System.out.println("name="+name);
                 System.out.println("trick="+trick);
                 System.out.println("==========");
            }
       }
       catch(ClassNotFoundException ce){
            // if the driver class not found, then we will be here
            System.out.println(ce.getmessage());
       }
       catch(SQLException e){
            // something went wrong, we are handling the exception here
            if ( conn != null ){
                conn.rollback();
                conn.setAutoCommit(true);
            }

            System.out.println("--- SQLException caught ---");
            // iterate and get all of the errors as much as possible.
            while ( e != null ){
                System.out.println("Message   : " + e.getMessage());
                System.out.println("SQLState  : " + e.getSQLState());
                System.out.println("ErrorCode : " + e.getErrorCode());
                System.out.println("---");
                e = e.getNextException();
            }
       }
       finally { // close db resources
          DatabaseUtil.close(rs);
          DatabaseUtil.close(stmt);
          DatabaseUtil.close(conn);
      }
   }
}