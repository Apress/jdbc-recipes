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
        public class Insert_Records_Using_PreparedStatement {

           public static void main(String[] args) {
               String dbVendor = args[0]; // { "mysql", "oracle" }
               int deptNumber = Integer.parseInt(args[1]);
               String deptName = args[2];
               String deptLocation = args[3];

               Connection conn = null;
               PreparedStatement pstmt = null;
               try {
                   System.out.println("--Insert_Records_ begin--");
                   conn = VeryBasicConnectionManager.getConnection(dbVendor);
                   System.out.println("conn="+conn);
                   System.out.println("deptNumber= "+ deptNumber);
                   System.out.println("deptName= "+ deptName);
                   System.out.println("deptLocation= "+ deptLocation);

                   // prepare query
                   String query = "insert into dept(DEPT_NUM, DEPT_NAME, DEPT_LOC) " +
                           "values(?, ?, ?)";

                   pstmt = conn.prepareStatement(query); // create a statement
                   pstmt.setInt(1, deptNumber);          // set input parameter 1
                   pstmt.setString(2, deptName);         // set input parameter 2
                   pstmt.setString(3, deptLocation);     // set input parameter 3
                   pstmt.executeUpdate();                // execute insert statement
                   System.out.println("--Insert_Records_ end--");
               }
               catch(Exception e){
                   e.printStackTrace();
                   System.exit(1);
               }
               finally {
                   // release database resources
                   DatabaseUtil.close(pstmt);
                   DatabaseUtil.close(conn);
               }
           }
       }
