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
 public class Demo_PreparedStatement_SetBoolean {

    public static void main(String[] args) {
        String dbVendor = args[0]; // values are: { "mysql", "oracle" }
        String idValue = args[1];
        boolean booleanValue;
        if (args[2].equals("0")) {
            booleanValue = false;
        }
        else {
            booleanValue = true;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("--Demo_PreparedStatement_setBoolean begin--");
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query =
              "insert into boolean_table(id, boolean_column) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, idValue);
            pstmt.setBoolean(2, booleanValue);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_setBoolean end--");
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
