 import java.sql.*;

 import jcb.util.DatabaseUtil;
 import jcb.db.VeryBasicConnectionManager;

 /**
  *
  * @author Mahmoud Parsian
  * @email  admin@jdbccookbook.com
  *
  */
  public class CheckJDBCInstallation {
     /**
      * Test Validity of JDBC Installation
      * @param conn a JDBC connection object
      * @param dbVendor db vendor {"oracle", "mysql" }
      * @return true if a given connection object is
      * a valid one; otherwise return false.
      * @throws Exception Failed to determine if a given
      * connection is valid.
      */
     public static boolean isValidConnection(Connection conn,
                                             String dbVendor)
         throws Exception {

         if (conn == null) {
             // null connection object is not valid
             return false;
         }

         if (conn.isClosed()) {
             // closed connection object is not valid
             return false;
         }

         // here you have a Connection object which is not null and
         // which is not closed, but it might be a defunct object
         // in order to determine whether it is a valid connection,
         // depends on the vendor of the database:
         //
         // for MySQL database:
         //      you may use the connection object
         //      with query of "select 1"; if the
         //      query returns the result, then it
         //      is a valid Connection object.
         //
         // for Oracle database:
         //      you may use the Connection object
         //      with query of "select 1 from dual"; if
         //      the query returns the result, then it
         //      is a valid Connection object.
         if (dbVendor.equalsIgnoreCase("mysql")) {
             return testConnection(conn, "select 1");
         }
         else if (dbVendor.equalsIgnoreCase("oracle")) {
             return testConnection(conn, "select 1 from dual");
         }
         else {
             return false;
         }

     }

     /**
      * Test Validity of a Connection
      * @param conn a JDBC connection object
      * @param query a sql query to test against db connection
      * @return true if a given connection object is a valid one;
      *  otherwise return false.
      */
     public static boolean testConnection(Connection conn,
                                          String query) {
         ResultSet rs = null;
         Statement stmt = null;
         try {
             stmt = conn.createStatement();
             if (stmt == null) {
                 return false;
             }

             rs = stmt.executeQuery(query);
             if (rs == null) {
                 return false;
             }

             // connection object is valid: you were able to
             // connect to the database and return something useful.
             if (rs.next()) {
                 return true;
             }

             // there is no hope any more for the validity
             // of the Connection object
             return false;
         }
         catch(Exception e) {
             // something went wrong: connection is bad
             return false;
         }
         finally {
             // close database resources
             DatabaseUtil.close(rs);
             DatabaseUtil.close(stmt);
         }
     }

     public static void main(String[] args) {
         Connection conn = null;
         try {
             System.out.println("-- CheckJDBCInstallation begin --");
             String dbVendor = args[0];
             // get connection to a database
             System.out.println("dbVendor="+dbVendor);
             conn = VeryBasicConnectionManager.getConnection(dbVendor);
             System.out.println("conn="+conn);
             System.out.println("valid connection = "+
                  isValidConnection(conn, dbVendor));
             System.out.println("-- CheckJDBCInstallation end --");
         }
         catch(Exception e){
             // handle the exception
             e.printStackTrace();
             System.exit(1);
         }
         finally {
             // release database resources
             DatabaseUtil.close(conn);
         }
     }
 }
