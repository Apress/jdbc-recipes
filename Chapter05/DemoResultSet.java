import java.sql.*;

import jcb.db.VeryBasicConnectionManager;
import jcb.util.DatabaseUtil;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DemoResultSet {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("--DemoResultSet begin--");
            String dbVendor = args[0]; // { "mysql", "oracle" }
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query = "select id, name, age from employees";

            // create a statement
            stmt = conn.createStatement();

            // execute query and return result as a ResultSet
            rs = stmt.executeQuery(query);

            // extract data from the ResultSet
            while (rs.next()) {
                String id = rs.getString(1);
                System.out.println("id="+id);
                String name = rs.getString(2);
                System.out.println("name="+name);
                // age might be null (according to schema)
                int age = rs.getInt(3);
                if (rs.wasNull()) {
                    System.out.println("age=null");
                }
                else {
                    System.out.println("age="+age);
                }
                System.out.println("---------------");
            }
            System.out.println("--DemoResultSet end--");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
            DatabaseUtil.close(conn);
        }
    }
}
