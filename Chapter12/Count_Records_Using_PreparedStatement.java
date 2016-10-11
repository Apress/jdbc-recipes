     import java.util.*;
     import java.io.*;
     import java.sql.*;

     import jcb.util.DatabaseUtil;
     import jcb.db.VeryBasicConnectionManager;

     /**
      *
      * @author Mahmoud Parsian
      * @email  admin@jdbccookbook.com
      *
      */
      public class Count_Records_Using_PreparedStatement {

         public static void main(String[] args) {
             String dbVendor = args[0];  // { "mysql", "oracle" }
             String tableName = args[1]; // table to be counted

             ResultSet rs = null;
             Connection conn = null;
             PreparedStatement pstmt = null;
             try {
                 System.out.println("--Count_Records_ begin--");
                 conn = VeryBasicConnectionManager.getConnection(dbVendor);
                 System.out.println("conn="+conn);

                 // prepare query
                 String query = "select count(*) from " + tableName;
                 pstmt = conn.prepareStatement(query); // create a statement
                 rs = pstmt.executQuery();             // count records
                 // get the number of rows from result set
                 if (rs.next()) {
                    int numberOfRows = rs.getInt(1);
                    System.out.println("numberOfRows= "+numberOfRows);
                 }
                 else {
                    System.out.println("error: could not get the record counts");
                    System.exit(1);
                 }

                 System.out.println("--Count_Records_ end--");
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
