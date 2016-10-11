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
 public class CountRows {

    public static int countRows(Connection conn, String tableName)
        throws SQLException {
        // select the number of rows in the table
        Statement stmt = null;
        ResultSet rs = null;
        int rowCount = -1;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) FROM "+ tableName);
            // get the number of rows from the result set
            if (rs.next()) {
               rowCount = rs.getInt(1);
            }
        }
        finally {
            DatabaseUtil.close(rs);
            DatabaseUtil.close(stmt);
        }
        return rowCount;
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            System.out.println("--CountRows begin--");
            String dbVendor = args[0];  // database vendor
            String tableName = args[1]; // table name
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("tableName="+tableName);
            System.out.println("conn="+conn);
            System.out.println("rowCount="+countRows(conn, tableName));
            System.out.println("--CountRows end--");
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
