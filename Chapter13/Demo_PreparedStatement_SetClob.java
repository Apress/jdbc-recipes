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
 public class Demo_PreparedStatement_SetClob {

    public static void main(String[] args) {
        System.out.println("--Demo_PreparedStatement_SetCharacterStream begin--");
         // read inputs from command line
        String dbVendor = args[0]; // {"mysql", "oracle" }
        String id = args[1];
        String newID = args[2];

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            System.out.println("--Demo_PreparedStatement_SetClob begin--");

            // get a database connection object
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // begin transaction
            conn.setAutoCommit(false);

            // prepare blob object from an existing binary column
            String query1 = "select clob_column from clob_table where id = ?";
            pstmt = conn.prepareStatement(query1);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            rs.next();
            java.sql.Clob clob = (java.sql.Clob) rs.getObject(1);
            // prepare SQL query for inserting a new row using setClob()
            String query = "insert into clob_table(id, clob_column) values(?, ?)";

            // create PrepareStatement object
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, newID);
            pstmt.setClob(2, clob);

            // execute query, and return number of rows created
            int rowCount = pstmt.executeUpdate();
            System.out.println("rowCount="+rowCount);

            // end transaction
            conn.commit();
            System.out.println("--Demo_PreparedStatement_SetClob end--");
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
