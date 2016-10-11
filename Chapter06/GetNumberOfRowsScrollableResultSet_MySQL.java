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
 public class GetNumberOfRowsScrollableResultSet_MySQL {

    public static Connection getConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/octopus";
        String username = "root";
        String password = "root";
        Class.forName(driver);  // load MySQL driver
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("------GetNumberOfRowsScrollableResultSet_MySQL begin---------");

            conn = getConnection();
            System.out.println("conn="+conn);
            System.out.println("---------------");

            //
            // prepare query
            //
            String query = "select id from employees";

            //
            // create a statement
            //
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

            //
            // execute query and return result as a ResultSet
            //
            rs = stmt.executeQuery(query);

            //
            // extract data from the ResultSet
            // scroll from top
            //
            while (rs.next()) {
                String id = rs.getString(1);
                System.out.println("id=" + id);
            }
            System.out.println("---------------");

            //
            // move to the end of the result set
            //
            rs.last();

            // get the row number of the last row which is also the row count
            int rowCount = rs.getRow();
            System.out.println("rowCount="+rowCount);

            System.out.println("------GetNumberOfRowsScrollableResultSet_MySQL end---------");
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