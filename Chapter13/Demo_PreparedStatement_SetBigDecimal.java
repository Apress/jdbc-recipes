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
 public class Demo_PreparedStatement_SetBigDecimal {

    public static void main(String[] args) {
        String dbVendor = args[0]; // { "mysql", "oracle" }
        String id = args[1];
        java.math.BigDecimal bigDecimal = new java.math.BigDecimal(args[2]);
        Connection conn = null;
        PreparedStatement pstmt = null;
        String query = null;
        try {
            System.out.println("--Demo_PreparedStatement_setBigDecimal begin--");

            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare SQL query
            query = "insert into  BIG_DECIMAL_TABLE(id, big_decimal) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setBigDecimal(2, bigDecimal);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_setBigDecimal end--");
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
