 import java.sql.ResultSet;
 import java.sql.Statement;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;

 import jcb.util.DatabaseUtil;
 import jcb.db.VeryBasicConnectionManager;

 /**
  *
  * @author Mahmoud Parsian
  * @email  admin@jdbccookbook.com
  *
  */
  public class DemoScrollableResultSet {

    public static void main(String[] args) {
      Connection conn = null;
      Statement stmt = null;
      ResultSet rs = null;
      String dbVendor = args[0];  // vendor = {“mysql”, “oracle” }
      try {
         conn = VeryBasicConnectionManager.getConnection(dbVendor);
         System.out.println("--DemoScrollableResultSet begin--");
         System.out.println("conn="+conn);
         System.out.println("-------");

         // prepare query
         String query = "select id, name from employees";

         // create a statement
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                     ResultSet.CONCUR_READ_ONLY);

         // execute query and return result as a ResultSet
         rs = stmt.executeQuery(query);

         // extract data from the ResultSet
         // scroll from top
         while (rs.next()) {
             String id = rs.getString(1);
             String name = rs.getString(2);
             System.out.println("id=" + id + "  name=" + name);
         }
         System.out.println("---------");

         // scroll from the bottom
         rs.afterLast();
         while (rs.previous()) {
             String id = rs.getString(1);
             String name = rs.getString(2);
             System.out.println("id=" + id + "  name=" + name);
         }
         System.out.println("---------");

         System.out.println("--DemoScrollableResultSet end--");
      }
      catch(Exception e){
         e.printStackTrace();
         System.exit(1);
      }
      finally {
          // release database resources
          DatabaseUtil.close(rs);
          DatabaseUtil.close(stmt);
          DatabaseUtil.close(conn);
      }
    }
 }
