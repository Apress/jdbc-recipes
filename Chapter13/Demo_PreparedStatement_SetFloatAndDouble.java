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
 public class Demo_PreparedStatement_SetFloatAndDouble {
    public static void main(String[] args) {
        System.out.println("--Demo_PreparedStatement_SetFloatAndDouble begin--");
         // read inputs from command line
        String dbVendor = args[0];
        String stringValue = args[1];
        float floatValue = Float.parseFloat(args[2]);
        double doubleValue = Double.parseDouble(args[3]);
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query = "insert into double_table( " +
                "id, float_column, double_column) values(?, ?, ?)";
            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, stringValue);
            pstmt.setFloat(2, floatValue);
            pstmt.setDouble(3, doubleValue);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_SetFloatAndDouble end--");
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
