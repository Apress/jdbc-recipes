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
           public class Delete_Records_Using_PreparedStatement {

              public static void main(String[] args) {
                  String dbVendor = args[0]; // { "mysql", "oracle" }
                  String tableName = args[1]; // table to be deleted
                  Connection conn = null;
                  PreparedStatement pstmt = null;
                  try {
                      System.out.println("--Delete_Records_ begin--");
                      conn = VeryBasicConnectionManager.getConnection(dbVendor);
                      System.out.println("conn="+conn);

                      // prepare query
                      String query = "delete from" + tableName;
                      pstmt = conn.prepareStatement(query); // create a statement
                      pstmt.executeUpdate();                // execute delete statement
                      System.out.println("--Delete_Records_ end--");
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
