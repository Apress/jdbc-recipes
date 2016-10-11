     import java.sql.*;
     import jcb.util.DatabaseUtil;
     import jcb.db.VeryBasicConnectionManager;

     /**
      *
      * @author Mahmoud Parsian
      * @email  admin@jdbccookbook.com
      *
      */
      public class CheckStatementPooling {
         public static boolean supportsStatementPooling(Connection conn)
            throws SQLException {
             if ((conn == null) || (conn.isClosed())) {
                return false;
             }

             DatabaseMetaData dbmd = conn.getMetaData();
             if (dbmd == null) {
                // database meta data NOT supported...
                // you should throw an exception or ... stop here
                System.out.println("can not determine if statement "+
                   "pooling is supported or not.");
                return false;
             }

             if (dbmd.supportsStatementPooling ()) {
                 // statement pooling is supported
                 return true;
             }
             else {
                 // statement pooling is NOT supported
                 return false;
             }
         }

         public static void main(String[] args) {
             String dbVendor = args[0];  // { "mysql", "oracle" }
             Connection conn = null;
             try {
                 System.out.println("--CheckStatementPooling begin--");
                 conn = VeryBasicConnectionManager.getConnection(dbVendor);
                 System.out.println("supportsStatementPooling="+
                         supportsStatementPooling(conn));
                 System.out.println("--CheckStatementPooling end--");
             }
             catch(Exception e){
                 e.printStackTrace();
                 System.exit(1);
             }
             finally {
                 // release database resources
                 DatabaseUtil.close(conn);
             }
         }
     }
