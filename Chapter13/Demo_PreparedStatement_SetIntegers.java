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
 public class Demo_PreparedStatement_SetIntegers {

    public static void main(String[] args) {
        String dbVendor = args[0]; // {"mysql", "oracle" }
        String id = args[1];
        byte byteValue = Byte.parseByte(args[2]);
        short shortValue = Short.parseShort(args[3]);
        int intValue = Integer.parseInt(args[4]);
        long longValue = Long.parseLong(args[5]);

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("--Demo_PreparedStatement_SetIntegers begin--");
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query = "insert into integer_table(id, byte_column, " +
               "short_column, int_column, long_column) values(?, ?, ?, ?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setByte(2, byteValue);
            pstmt.setShort(3, shortValue);
            pstmt.setInt(4, intValue);
            pstmt.setLong(5, longValue);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_SetIntegers end--");
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
