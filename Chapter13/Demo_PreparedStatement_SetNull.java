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
 public class Demo_PreparedStatement_SetNull {
    public static void main(String[] args) {
        System.out.println("--Demo_PreparedStatement_SetNull begin--");
         // read inputs from command line
        String dbVendor = args[0];
        String idValue = args[1];
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query = "insert into nullable_table(id, " +
                "string_column, int_column) values(?, ?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, idValue);
            pstmt.setNull(2, java.sql.Types.VARCHAR);
            pstmt.setNull(3, java.sql.Types.INTEGER);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_SetNull end--");
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
