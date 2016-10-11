import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

import jcb.util.DatabaseUtil;
import jcb.db.VeryBasicConnectionManager;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class CreateEmployeeTable {

    private static final String EMPLOYEE_TABLE =
        "create table MyEmployees3 ( " +
        "   id INT PRIMARY KEY, " +
        "   firstName VARCHAR(20), " +
        "   lastName VARCHAR(20), " +
        "   title VARCHAR(20), " +
        "   salary INT " +
        ")";

    public static void main(String args[]) {
        Connection conn = null;
        Statement stmt = null;
        System.out.println("---CreateEmployeeTable begin---");
        String dbVendor = args[0];  // database vendor
        try {
          conn = VeryBasicConnectionManager.getConnection(dbVendor);
          stmt = conn.createStatement();
          stmt.executeUpdate(EMPLOYEE_TABLE);
          stmt.executeUpdate("insert into MyEmployees3(id, firstName) "+
            "values(100, 'Alex')");
          stmt.executeUpdate("insert into MyEmployees3(id, firstName) "+
            "values(200, 'Mary')");
          System.out.println("---CreateEmployeeTable: table created---");
        }
        catch(ClassNotFoundException ce) {
          System.out.println("error: failed to load JDBC driver.");
          ce.printStackTrace();
        }
        catch(SQLException se) {
          System.out.println("JDBC error:" +se.getMessage());
          se.printStackTrace();
        }
        catch(Exception e) {
          System.out.println("other error:"+e.getMessage());
          e.printStackTrace();
        }
        finally {
            // close JDBC/database resources
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(conn);
        }
    }

}
