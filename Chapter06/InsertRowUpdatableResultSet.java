 import java.sql.*;

 import jcb.util.DatabaseUtil;
 import jcb.db.VeryBasicConnectionManager;

 /**
  *
  * @author Mahmoud Parsian
  * @email  admin@jdbccookbook.com
  *
  */
  public class InsertRowUpdatableResultSet {

     public static void main(String[] args) {
         Connection conn = null;
         Statement stmt = null;
         ResultSet rs = null;
         try {
             String dbVendor = args[0];  // vendor = {"mysql", "oracle" }
             System.out.println("--InsertRowUpdatableResultSet begin--");
             conn = VeryBasicConnectionManager.getConnection(dbVendor);
             System.out.println("conn="+conn);

             // prepare query
             String query = "select id, name from employees";

             // create a statement
             stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                         ResultSet.CONCUR_UPDATABLE);
             // execute query and return result as a ResultSet
             rs = stmt.executeQuery(query);

             // extract data from the ResultSet
             // scroll from top
             while (rs.next()) {
                 String id = rs.getString(1);
                 String name = rs.getString(2);
                 System.out.println("id=" + id + "  name=" + name);
             }
             System.out.println("=======");

             //  Move cursor to the "insert row"
             rs.moveToInsertRow();

             // Set values for the new row.
             rs.updateString("id", args[1]);
             rs.updateString("name", args[2]);

             // Insert the new row
             rs.insertRow();

             // scroll from the top again
             rs.beforeFirst();
             while (rs.next()) {
                 String id = rs.getString(1);
                 String name = rs.getString(2);
                 System.out.println("id=" + id + "  name=" + name);
             }
             System.out.println("--InsertRowUpdatableResultSet end--");
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
