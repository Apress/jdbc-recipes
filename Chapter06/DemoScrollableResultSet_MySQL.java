import java.util.*;
import java.io.*;
import java.sql.*;

import jcb.db.*;
import jcb.meta.*;

/**
 *
 * @author Mahmoud Parsian
 * @email  admin@jdbccookbook.com
 *
 */
 public class DemoScrollableResultSet_MySQL {

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("--DemoScrollableResultSet_MySQL begin--");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("-------");

            // prepare query
            String query = "select id, name from employees";

            // create a statement
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

            // execute query and return result as a ResultSet
            rs = stmt.executeQuery(query);

            // extract data from the ResultSet
            // scroll from top
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                System.out.println("id=" + id + "  name=" + name);
            }
            System.out.println("---------");

            // scroll from the bottom
            rs.afterLast();
            while (rs.previous()) {
                String id = rs.getString(1);
                String name = rs.getString(2);
                System.out.println("id=" + id + "  name=" + name);
            }
            System.out.println("---------");

            System.out.println("--DemoScrollableResultSet_MySQL end--");
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