import java.util.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DeleteSubtree {

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/snipit";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        return DriverManager.getConnection(url, username, password);
    }

    public static void usage() {
        System.out.println("usage: DeleteSubtree   ");
        System.out.println("Example-1: DeleteSubtree  Bob");
        System.exit(1);
    }

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        try {
            System.out.println("------DeleteSubtree begin---------");

            if (args.length != 1) {
                usage();
            }

            conn = getConnection();
            System.out.println("conn="+conn);

            // subtree/node with value of ID will be deleted
            String ID = args[0];
            System.out.println("ID="+ID);
            String getLeftRight =
               "select lft, rgt from folders where id = ?";
            pstmt = conn.prepareStatement(getLeftRight);
            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int delLeft = rs.getInt(1);
            int delRight = rs.getInt(2);
            int delta = delRight - delLeft + 1;

            // start transaction for batch updates
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            String deleteID = "delete from folders "+
              "where lft between "+delLeft+" and "+delRight;
            String update1 = "update folders set lft = lft - "+delta+
              "  where lft > "+delLeft;
            String update2 = "update folders set rgt = rgt - "+delta+
              "  where rgt > "+delRight;
            stmt.addBatch(deleteID);
            stmt.addBatch(update1);
            stmt.addBatch(update2);

             // send batch operations to the database server
             int[] batchUpdateCounts = stmt.executeBatch();

             // commit transaction for batch updates
             conn.commit();
             conn.setAutoCommit(true);
             System.out.println("------DeleteSubtree end---------");
         }
         catch (Exception e) {
             e.printStackTrace();
             System.exit(1);
         }
         finally {
             // release database resources
         }
     }
 }
