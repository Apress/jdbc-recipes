 import java.util.*;
 import java.io.*;
 import java.sql.*;

 /**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DeleteNode {

     public static Connection getConnection() throws Exception {
         String driver = "org.gjt.mm.mysql.Driver";
         String url = "jdbc:mysql://localhost/snipit";
         String username = "root";
         String password = "root";
         Class.forName(driver);  // load MySQL driver
         return DriverManager.getConnection(url, username, password);
     }

       public static void usage() {
           System.out.println("usage: DeleteNode   ");
           System.out.println("Example-1: DeleteNode  Bob true");
           System.out.println("Example-2: DeleteNode  Bob false");
           System.exit(1);
       }

       public static void main(String[] args) {
           Connection conn = null;
           PreparedStatement pstmt = null;
           try {
               System.out.println("------DeleteNode begin---------");

               if (args.length != 2) {
                   usage();
               }

               conn = getConnection();
               System.out.println("conn="+conn);

               String ID = args[0]; // node with value of ID will be deleted
               String promoteSubtreeAsString = args[1];

               System.out.println("ID="+ID);
               System.out.println("promoteSubtree="+promoteSubtreeAsString);

               if ((promoteSubtreeAsString == null) ||
                   (promoteSubtreeAsString.length() == 0)) {
                   usage();
               }

               boolean promoteSubtree = false;
             if (promoteSubtreeAsString.equals("true")) {
                 promoteSubtree = true;
             }
             else if (promoteSubtreeAsString.equals("false")) {
                 promoteSubtree = false;
             }
             else {
                 usage();
             }
             String getLeftRight =
               "select parent, lft, rgt from folders where id = ?";
             pstmt = conn.prepareStatement(getLeftRight);
             pstmt.setString(1, ID);
             ResultSet rs = pstmt.executeQuery();
             rs.next();
             String parentID = rs.getString(1);
             int delLeft = rs.getInt(2);
             int delRight = rs.getInt(3);
             System.out.println("ID="+ID);
             System.out.println("parentID="+parentID);
             System.out.println("delLeft="+delLeft);
             System.out.println("delRight="+delRight);

             if(promoteSubtree) {
                 // promote the subtree
                 promoteTheSubTree(conn, ID, parentID, delLeft, delRight);
             }
             else {
                 // promote the leftmost sibiling to the new parent
                 promoteSibiling(conn, ID, parentID, delLeft, delRight);
             }


             System.out.println("------DeleteNode end---------");
           }
           catch (Exception e) {
               e.printStackTrace();
               System.exit(1);
           }
           finally {
               // release database resources
         }
     }


     public static void promoteTheSubTree(Connection conn,
                                          String ID,
                                          String parentID,
                                          int delLeft,
                                          int delRight)
         throws SQLException, BatchUpdateException {

          // start transaction for batch updates
          conn.setAutoCommit(false);
          Statement stmt = conn.createStatement();

          String deleteID = "delete from folders where ID = '"+ID+"'";
          String update1 = "update folders set lft = lft - 1, "+
           "rgt = rgt - 1 where lft between "+delLeft+" and "+delRight;
          String update2 = "update folders set rgt = rgt - 2 "+
           "where rgt > "+delRight;
          String update3 = "update folders set lft = lft - 2 "+
           "where lft > "+delRight;
          String update4 = "update folders set parent = '"+parentID+
           "' where parent='"+ID+"'";
          stmt.addBatch(deleteID);
          stmt.addBatch(update1);
          stmt.addBatch(update2);
          stmt.addBatch(update3);
          stmt.addBatch(update4);

          // send batch operations to the database server
          int[] batchUpdateCounts = stmt.executeBatch();

          // commit transaction for batch updates
          conn.commit();
          conn.setAutoCommit(true);
      }

      public static void promoteSibiling(Connection conn,
                                         String ID,
                                         String parentID,
                                         int delLeft,
                                         int delRight)
          throws SQLException, BatchUpdateException {

          // promote the leftmost sibiling to the new parent
          // find the new parent's ID (which is the new promoted node ID)
          // then set the parent of siblings to the found id
          int delLeftPlus1 = delLeft + 1;
          String findID = "select id from folders where lft = "+delLeftPlus1;
          Statement findStmt = conn.createStatement();
          ResultSet rs = findStmt.executeQuery(findID);
          rs.next();
          String newParentID = rs.getString(1);
          rs.close();
          System.out.println("newParentID="+newParentID);

          // start transaction for batch updates
          conn.setAutoCommit(false);
          Statement stmt = conn.createStatement();

          // set the new parents for promoted node's sibiling
          String updateParents = "update folders set parent = '"+
           newParentID+"' where lft > "+delLeft+" and rgt < "+delRight;
          String update1 = "update folders set lft = lft - 1, "+
           "rgt = "+delRight+", parent= '"+parentID+
           "' where lft = "+delLeftPlus1;
          String update2 = "update folders set rgt = rgt - 2 "+
           " where rgt > "+delLeft;
          String update3 = "update folders set lft = lft - 2 "+
           "where lft > "+delLeft;
          String deleteID = "delete from folders where ID = '"+ID+"'";
          stmt.addBatch(updateParents);
          stmt.addBatch(update1);
          stmt.addBatch(update2);
          stmt.addBatch(update3);
          stmt.addBatch(deleteID);

          // send batch operations to the database server
          int[] batchUpdateCounts = stmt.executeBatch();

          // commit transaction for batch updates
          conn.commit();
          conn.setAutoCommit(true);
      }
  }
