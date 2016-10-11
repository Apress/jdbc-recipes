       import java.util.*;
       import java.io.*;
       import java.sql.*;

       import jcb.db.VeryBasicConnectionManager;
       import jcb.util.DatabaseUtil;

       /**
        *
        * @author Mahmoud Parsian
        * @email  admin@jdbccookbook.com
        *
        */
        public class DemoUpdatableResultSet {
           public static void main(String[] args) {
               ResultSet rs = null;
               Connection conn = null;
               PreparedStatement pstmt = null;
               try {
                   System.out.println("--DemoUpdatableResultset begin--");
                   // read command line arguments
                   String dbVendor = args[0]; // database vendor
                   int ageLimit = Integer.parseInt(args[1]); // age limit

                   conn = VeryBasicConnectionManager.getConnection(dbVendor);
                   System.out.println("conn="+conn);
                   System.out.println("ageLimit="+ageLimit);
                   String query = "select id, name, age from employees where age > ?";

                   // create a PreparedStatement, which will
                   // create an updatable ResultSet object
                   pstmt = conn.prepareStatement(query,
                               ResultSet.TYPE_SCROLL_SENSITIVE,
                               ResultSet.CONCUR_UPDATABLE);
                   pstmt.setInt(1, ageLimit);  // set input values
                   rs = pstmt.executeQuery();  // create an updatable ResultSet

                   //
                   // update a column value in the current row.
                   //
                   // moves the cursor to the 2nd row of rs
                   rs.absolute(2);
                   // updates the NAME column of row 2 to be NEW-NAME
                   rs.updateString("NAME", "NEW-NAME");
                   // updates the row in the data source
                   rs.updateRow();

                   //
                   // insert column values into the insert row.
                   //
                   rs.moveToInsertRow(); // moves cursor to the insert row
                   rs.updateInt(1, 5000); // 1st column id=5000
                   rs.updateString(2, "NEW-NAME-IS-HERE"); // updates the 2nd column
                   rs.updateInt(3, 99); // updates the 3rd column to 99
                   rs.insertRow();
                   rs.moveToCurrentRow();

                   System.out.println("--DemoUpdatableResultset end--");
               }
               catch(Exception e){
                   e.printStackTrace();
                   System.exit(1);
               }
               finally {
                   // release database resources
                   DatabaseUtil.close(rs);
                   DatabaseUtil.close(pstmt);
                   DatabaseUtil.close(conn);
               }
           }
       }
