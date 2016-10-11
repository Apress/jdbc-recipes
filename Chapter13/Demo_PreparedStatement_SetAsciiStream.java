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
 public class Demo_PreparedStatement_SetAsciiStream {

    public static void main(String[] args) {
        String dbVendor = args[0]; // { "mysql", "oracle" }
        String fileName = args[1];
        Connection conn = null;
        PreparedStatement pstmt = null;
        String query = null;
        try {
            System.out.println("--Demo_PreparedStatement_setAsciiStream begin--");
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare text stream
            File file = new File(fileName);
            int fileLength = (int) file.length();
            InputStream stream = (InputStream) new FileInputStream(file);

            // prepare SQL query
            query = "insert into  LONG_VARCHAR_TABLE(id, stream) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, fileName);
            pstmt.setAsciiStream(2, stream, fileLength);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_setAsciiStream end--");
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
