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
 public class Demo_PreparedStatement_SetDate {
    public static java.sql.Date getCurrentJavaSqlDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }
    public static void main(String[] args) {
        System.out.println("--Demo_PreparedStatement_SetDate begin--");
         // read inputs from command line
        String dbVendor = args[0];
        String id = args[1];
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query = "insert into date_table(id, date_column) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            java.sql.Date date = getCurrentJavaSqlDate();
            pstmt.setDate(2, date);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_SetDate end--");
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
