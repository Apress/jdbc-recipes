import java.sql.*;
import jcb.util.DatabaseUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class GetDate {

   public static Connection getConnection() throws Exception {
      String driver = "oracle.jdbc.driver.OracleDriver";
      String url = "jdbc:oracle:thin:@localhost:1521:scorpian";
      Class.forName(driver);
      System.out.println("ok: loaded oracle driver.");
      return DriverManager.getConnection(url, "scott", "tiger");
   }

   public static void main(String args[]) {
      String GET_RECORD = "select date_column, time_column, "+
        "timestamp_column from TestDates where id = ?";
      ResultSet rs = null;
      Connection conn = null;
      PreparedStatement pstmt = null;
      try {
          conn = getConnection();
          pstmt = conn.prepareStatement(GET_RECORD);
          pstmt.setString(1, "id100");
          rs = pstmt.executeQuery();
          if (rs.next()) {
             java.sql.Date dbSqlDate = rs.getDate(1);
             java.sql.Time dbSqlTime = rs.getTime(2);
             java.sql.Timestamp dbSqlTimestamp = rs.getTimestamp(3);
             System.out.println("dbSqlDate="+dbSqlDate);
             System.out.println("dbSqlTime="+dbSqlTime);
             System.out.println("dbSqlTimestamp="+dbSqlTimestamp);
         }
      }
      catch( Exception e ) {
         e.printStackTrace();
         System.out.println("Failed to insert the record.");
         System.exit(1);
      }
      finally {
          DatabaseUtil.close(rs);
          DatabaseUtil.close(pstmt);
          DatabaseUtil.close(conn);
      }
   }
}
