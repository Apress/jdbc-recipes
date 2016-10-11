import java.sql.*;
import jcb.util.DatabaseUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class TestNormalization {

   public static Connection getConnection() throws Exception {
      String driver = "oracle.jdbc.driver.OracleDriver";
      String url = "jdbc:oracle:thin:@localhost:1521:caspian";
      Class.forName(driver);
      System.out.println("ok: loaded oracle driver.");
      return DriverManager.getConnection(url, "scott", "tiger");
   }

    /**
     * Create an MySQL Connection...
     */
    public static Connection getMySQLConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        return DriverManager.getConnection(url, username, password);
    }

   public static void main(String args[]) {
      String GET_RECORDS =
        "select time_col, date_col, date_time_col from date_time_table";
      ResultSet rs = null;
      Connection conn = null;
      Statement stmt = null;
      try {
          conn = getConnection();
          stmt = conn.createStatement();
          rs = stmt.executeQuery(GET_RECORDS);
          while (rs.next()) {
             java.sql.Time dbSqlTime = rs.getTime(1);
             java.sql.Date dbSqlDate = rs.getDate(2);
             java.sql.Timestamp dbSqlTimestamp = rs.getTimestamp(3);
             System.out.println("dbSqlTime="+dbSqlTime);
             System.out.println("dbSqlDate="+dbSqlDate);
             System.out.println("dbSqlTimestamp="+dbSqlTimestamp);
             System.out.println("-- check for Normalization --");
             java.util.Date dbSqlTimeConverted = new java.util.Date(dbSqlTime.getTime());
             java.util.Date dbSqlDateConverted = new java.util.Date(dbSqlDate.getTime());
             System.out.println("dbSqlTimeConverted="+dbSqlTimeConverted);
             System.out.println("dbSqlDateConverted="+dbSqlDateConverted);
         }
      }
      catch( Exception e ) {
         e.printStackTrace();
         System.out.println("Failed to get the records.");
         System.exit(1);
      }
      finally {
          DatabaseUtil.close(rs);
          DatabaseUtil.close(stmt);
          DatabaseUtil.close(conn);
      }
   }
}


