import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import jcb.util.DatabaseUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class Test_JDBC_ODBC {
   public static Connection getConnection() throws Exception {
      String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
      String url = "jdbc:odbc:northwind";
      String username = "";
      String password = "";
      Class.forName(driver);   // load JDBC-ODBC driver
      return DriverManager.getConnection(url, username, password);
   }

   public static void main(String args[]) {
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      try {
         conn = getConnection();
         stmt = conn.createStatement();
         String query =
           "select EmployeeID, LastName, FirstName from Employees";
         rs = stmt.executeQuery(query);.
         while(rs.next()){
            System.out.println(rs.getString("EmployeeID")+
                  " "+ rs.getString("LastName")+
                  " "+ rs.getString("FirstName"));
         }
      }
      catch (Exception e){
         // handle the exception
         e.printStackTrace();
         System.err.println(e.getMessage());
      }
      finally {
         // release database resources
         DatabaseUtil.close(rs);     // close the ResultSet object
         DatabaseUtil.close(stmt);   // close the Statement object
         DatabaseUtil.close(conn);   // close the Connection object
      }
   }
