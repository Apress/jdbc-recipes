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
 public class Demo_PreparedStatement_SetRef {
    public static void main(String[] args) {
        System.out.println("--Demo_PreparedStatement_SetRef begin--");
         // read database vendor
        String dbVendor = args[0];
        // prepare arguments for dept_table
        String deptName = args[0];
        String newDeptName = args[1];

        ResultSet rs =  null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        try {
            conn = VeryBasicConnectionManager.getConnection(dbVendor);
            System.out.println("conn="+conn);
            System.out.println("---------------");

            // prepare query for getting a REF object and PrepareStatement object
            String refQuery = "select manager from dept_table where dept_name=?";
            pstmt = conn.prepareStatement(refQuery);
            pstmt.setString(1, deptName);
            rs = pstmt.executeQuery();
            java.sql.Ref ref = null;
            if (rs.next()) {
                ref = rs.getRef(1);
            }

            if (ref == null) {
                System.out.println("error: could not get a reference for manager.");
                System.exit(1);
            }

            // prepare query and create PrepareStatement object
            String query = "INSERT INTO dept_table(dept_name, manager) "+
                 "values(?, ?)";
            pstmt2 = conn.prepareStatement(query);
            pstmt2.setString(1, newDeptName);
            pstmt2.setRef(2, ref);

            // execute query, and return number of rows created
            int rowCount = pstmt2.executeUpdate();
            System.out.println("rowCount="+rowCount);
            System.out.println("--Demo_PreparedStatement_setRef end--");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        finally {
            // release database resources
            DatabaseUtil.close(pstmt);
            DatabaseUtil.close(pstmt2);
            DatabaseUtil.close(conn);
        }
    }
}
