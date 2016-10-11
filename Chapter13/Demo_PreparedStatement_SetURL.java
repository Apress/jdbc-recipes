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
 public class Demo_PreparedStatement_SetURL {
    public static void main(String[] args) {
        System.out.println("--SetURL begin--");
        String dbVendor = args[0];  // database vendor = { "mysql", "oracle" }
        String idValue = args[1];
        String urlValue = args[2];
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("--SetURL begin--");
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query
            String query = "insert into url_table(id, url) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, idValue);
            pstmt.setURL(2, new java.net.URL(urlValue));

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--SetURL end--");
        }
        catch(Exception e){
            System.out.println("ERROR: "+ e.getMessage());
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(conn);
        }
    }
}
