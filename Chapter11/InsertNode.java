   import java.sql.Connection;
   import java.sql.Statement;
   import java.sql.PreparedStatement;
   import java.sql.ResultSet;
   import java.sql.DriverManager;
   import java.sql.BatchUpdateException;
   import java.sql.SQLException;
   import jcb.util.DatabaseUtil;

   /**
    *
    * @author Mahmoud Parsian
    * @email  admin@jdbccookbook.com
    *
    */
    public class InsertNode {

       public static Connection getConnection() throws Exception {
           String driver = "org.gjt.mm.mysql.Driver";
           String url = "jdbc:mysql://localhost/snipit";
           String username = "root";
           String password = "root";
           Class.forName(driver);  // load MySQL driver
           return DriverManager.getConnection(url, username, password);
       }

       public static void main(String[] args) {
           Connection conn = null;
           ResultSet rs = null;
           Statement stmt = null;
          PreparedStatement pstmt = null;
           try {
               System.out.println("------InsertNode begin---------");

               if (args.length != 2) {
                   System.out.println("usage: InsertNode  ");
                   System.exit(1);
               }

               conn = getConnection();
               System.out.println("conn="+conn);

               String parentID = args[0];
               String ID = args[1]; // node with value of ID will be inserted

               System.out.println("parentID="+parentID);
               System.out.println("ID="+ID);

               String getParentLeftRight =
                 "select lft, rgt from folders where id = ?";
               pstmt = conn.prepareStatement(getParentLeftRight);
               pstmt.setString(1, parentID);
               rs = pstmt.executeQuery();
               rs.next();
               int pLeft = rs.getInt(1);
               int pRight = rs.getInt(2);
               int pLeftPlus1 = pLeft + 1;
               int pLeftPlus2 = pLeft + 2;
               String update1 =
                 "update folders set rgt = rgt + 2 where rgt > "+ pLeft;
               String update2 =
                 "update folders set lft = lft + 2 where lft > "+ pLeft;
               String insert = "insert into folders (id, parent, lft, rgt)"+
                 "values ('"+ID +"', '"+ parentID +"', 0, 0)";
               String update3 = "update folders set lft = "+pLeftPlus1+
                 ", rgt = "+pLeftPlus2+ " where id = '"+ID+"'";
               // start transaction for batch updates
               conn.setAutoCommit(false);
               stmt = conn.createStatement();

               // create a set of batch operations
               stmt.addBatch(update1);
               stmt.addBatch(update2);
               stmt.addBatch(insert);
               stmt.addBatch(update3);

               // send batch operations to the database server
               int[] batchUpdateCounts = stmt.executeBatch();

               // commit transaction for batch updates
               conn.commit();
               conn.setAutoCommit(true);
               System.out.println("------InsertNode end---------");
           }
           catch(BatchUpdateException be) {
               System.err.println("--- caught BatchUpdateException ---");
               System.err.println("SQLState:  " + be.getSQLState());
               System.err.println("Message:  " + be.getMessage());
               System.err.println("Vendor:  " + be.getErrorCode());
               System.err.print("Update counts are:  ");
               int[] batchUpdateCounts = be.getUpdateCounts();
               for (int i = 0; i < batchUpdateCounts.length; i++) {
                   System.err.print(batchUpdateCounts[i] + " ");
               }
               System.err.println("");
           }
           catch(SQLException se) {
               System.err.println("--- caught SQLException ---");
               System.err.println("SQLState:  " + se.getSQLState());
               System.err.println("Message:  " + se.getMessage());
               System.err.println("Vendor:  " + se.getErrorCode());
           }
           catch (Exception e) {
               // other exceptions
               e.printStackTrace();
               System.exit(1);
           }
          finally {
              // release database resources
              DatabaseUtil.close(rs);
              DatabaseUtil.close(pstmt);
              DatabaseUtil.close(stmt);
              DatabaseUtil.close(conn);
          }
      }
  }
