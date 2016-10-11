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
 public class Demo_PreparedStatement_SetTimeAndTimestamp {
    public static java.sql.Timestamp getCurrentJavaSqlTimestamp() {
        java.util.Date date = new java.util.Date();
        return new java.sql.Timestamp(date.getTime());
    }
    public static java.sql.Time getCurrentJavaSqlTime() {
        java.util.Date date = new java.util.Date();
        return new java.sql.Time(date.getTime());
    }
    public static void main(String[] args) {
        System.out.println("--SetTimeAndTimestamp begin--");
        String dbVendor = args[0]; // database vendor = { "mysql", "oracle" }
        String id = args[1];
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");
            // prepare query
            String query = "insert into time_table(id, "+
               "time_column, timestamp_column) values(?, ?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            java.sql.Time time = getCurrentJavaSqlTime();
            System.out.println("time="+time);
            pstmt.setTime(2, time);
            java.sql.Timestamp timestamp = getCurrentJavaSqlTimestamp();
            System.out.println("timestamp="+timestamp);
            pstmt.setTimestamp(3, timestamp);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--SetTimeAndTimestamp end--");
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
