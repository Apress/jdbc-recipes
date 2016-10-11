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
 public class Demo_PreparedStatement_SetBlob {

    public static void main(String[] args) {
        // set up input parameters from command line:
        String dbVendor = args[0]; // { "mysql", "oracle" }
        String id = args[1];

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        java.sql.Blob blob = null;
        try {
            System.out.println("--Demo_PreparedStatement_SetBlob begin--");

            // get a database Connection object
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare blob object from an existing binary column
            String query1 = "select photo from my_pictures where id = ?";
            pstmt = conn.prepareStatement(query1);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            blob = rs.getBlob(1);

            // prepare SQL query for inserting a new row using setBlob()
            String query = "insert into blob_table(id, blob_column) values(?, ?)";

            // begin transaction
            conn.setAutoCommit(false);

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setBlob(2, blob);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);

            // end transaction
            conn.commit();
            System.out.println("--Demo_PreparedStatement_SetBlob end--");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(rs);
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(conn);
        }
    }
}
