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
          public class Update_Records_Using_PreparedStatement {

              public static void main(String[] args) {
                  String dbVendor = args[0]; // { "mysql", "oracle" }
                  int deptNumber = Integer.parseInt(args[1]);
                  String deptLocation = args[2];

                  Connection conn = null;
                  PreparedStatement pstmt = null;
                  try {
                      System.out.println("--Update_Records_ begin--");
                      conn = VeryBasicConnectionManager.getConnection(dbVendor);
                      System.out.println("conn="+conn);
                      System.out.println("deptNumber= "+ deptNumber);
                      System.out.println("deptLocation= "+ deptLocation);

                      // prepare query
                      String query = "update dept set DEPT_LOC = ? where DEPT_NUM = ? ";
                      pstmt = conn.prepareStatement(query); // create a statement
                      pstmt.setString(1, deptLocation);     // set input parameter 1
                      pstmt.setInt(2, deptNumber);          // set input parameter 2
                      pstmt.executeUpdate();                // execute update statement
                      System.out.println("--Update_Records_ end--");
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
